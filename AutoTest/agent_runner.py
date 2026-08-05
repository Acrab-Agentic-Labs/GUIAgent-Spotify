# -*- coding: utf-8 -*-
"""
GUI Agent Runner for MySpotify AutoTest
Uses multimodal LLM with SoM (Set of Mark) annotation for precise UI interaction
"""

import argparse
import base64
import io
import json
import logging
import os
import subprocess
import sys
import time
import xml.etree.ElementTree as ET
from datetime import datetime

from dotenv import load_dotenv
from openai import OpenAI
from PIL import Image, ImageDraw, ImageFont
import uiautomator2 as u2

load_dotenv(override=True, verbose=True)

SYSTEM_PROMPT = """You are an Android app automation assistant. You are operating a music player app called MySpotify.

## App Structure
- Bottom navigation bar has three tabs: Home, Search, Your Library
- Bottom has a mini player bar showing the currently playing song
- Clicking the mini player opens the full-screen playback page
- Home page has recommended playlists, podcasts, audiobooks, etc.
- Search page has category browsing and search functionality
- Library has Liked Songs, followed artists, podcasts, and playlists

## How to Read the Screenshot
The screenshot has numbered markers on interactive UI elements. Each marker shows an index number.
You will also receive a text list of all UI elements with their index, text, and properties.

## Operation Rules
1. Execute only one operation at a time
2. Use element index to click/interact, or use coordinates for scroll
3. When task is complete or you need to answer a question, use the finish action

## Output Format
You must return ONLY a JSON object, no other text:
{"action": "action_type", "params": {}, "reason": "reason"}
"""


class UIElement:
    """Parsed UI element from hierarchy"""
    def __init__(self, index, bounds, text="", desc="", clickable=False,
                 long_clickable=False, editable=False, scrollable=False, focusable=False):
        self.index = index
        self.bounds = bounds  # (left, top, right, bottom)
        self.text = text
        self.desc = desc
        self.clickable = clickable
        self.long_clickable = long_clickable
        self.editable = editable
        self.scrollable = scrollable
        self.focusable = focusable

    @property
    def center(self):
        l, t, r, b = self.bounds
        return ((l + r) // 2, (t + b) // 2)


def parse_hierarchy(device):
    """Parse UI hierarchy into UIElement list"""
    elements = []
    try:
        xml_str = device.dump_hierarchy(compressed=False, pretty=False)
        if not xml_str:
            return elements
        root = ET.fromstring(xml_str)
    except Exception as e:
        logging.error(f"Failed to parse hierarchy: {e}")
        return elements

    index = 0
    for node in root.iter("node"):
        bounds_str = node.get("bounds", "")
        if not bounds_str:
            continue
        try:
            parts = bounds_str.replace("[", "").replace("]", ",").split(",")
            l, t, r, b = int(parts[0]), int(parts[1]), int(parts[2]), int(parts[3])
            if r <= l or b <= t:
                continue
        except (ValueError, IndexError):
            continue

        text = node.get("text") or ""
        desc = node.get("content-desc") or ""
        clickable = node.get("clickable", "false") == "true"
        long_clickable = node.get("long-clickable", "false") == "true"
        editable = node.get("editable", "false") == "true"
        scrollable = node.get("scrollable", "false") == "true"
        focusable = node.get("focusable", "false") == "true"

        is_interactive = clickable or long_clickable or editable or scrollable or focusable
        has_content = bool(text) or bool(desc)

        if not is_interactive and not has_content:
            continue

        # Skip status bar elements (top 128px system area)
        if b <= 128:
            continue

        elements.append(UIElement(
            index=index, bounds=(l, t, r, b),
            text=text, desc=desc,
            clickable=clickable, long_clickable=long_clickable,
            editable=editable, scrollable=scrollable, focusable=focusable
        ))
        index += 1

    return elements


def annotate_screenshot(screenshot_img, elements):
    """Draw SoM markers on screenshot"""
    draw = ImageDraw.Draw(screenshot_img)

    for el in elements:
        l, t, r, b = el.bounds
        cx, cy = el.center

        # Draw bounding box
        draw.rectangle([l, t, r, b], outline="red", width=2)

        # Draw index label
        label = str(el.index)
        # Background for label
        label_w = len(label) * 10 + 6
        label_h = 20
        lx = max(l, 0)
        ly = max(t - label_h - 2, 0)
        draw.rectangle([lx, ly, lx + label_w, ly + label_h], fill="red")
        draw.text((lx + 3, ly + 1), label, fill="white")

    return screenshot_img


def elements_to_text(elements):
    """Convert elements to text description for LLM"""
    lines = []
    for el in elements:
        desc_parts = [f"index={el.index}"]
        if el.text:
            desc_parts.append(f'text="{el.text}"')
        if el.desc:
            desc_parts.append(f'desc="{el.desc}"')
        if el.clickable:
            desc_parts.append("clickable")
        if el.editable:
            desc_parts.append("editable")
        if el.scrollable:
            desc_parts.append("scrollable")
        lines.append(f"[{', '.join(desc_parts)}]")
    return "\n".join(lines)


class SimpleAgent:
    """GUI Agent with SoM annotation"""

    def __init__(self, api_key, base_url, model_name, device_id, screenshots_dir):
        self.client = OpenAI(api_key=api_key, base_url=base_url)
        self.model_name = model_name
        self.device = u2.connect(device_id)
        self.screenshots_dir = screenshots_dir
        self.history = []

        info = self.device.info
        self.screen_width = info.get("displayWidth", 1080)
        self.screen_height = info.get("displayHeight", 2400)
        logging.info(f"Screen: {self.screen_width}x{self.screen_height}")

        os.makedirs(screenshots_dir, exist_ok=True)

    def reset(self):
        self.history = []

    def take_screenshot_and_annotate(self, save_path):
        """Take screenshot, parse UI, annotate, return (base64, elements)"""
        # Take screenshot
        screenshot = self.device.screenshot()

        # Parse UI hierarchy
        elements = parse_hierarchy(self.device)

        # Annotate screenshot
        annotated = annotate_screenshot(screenshot.copy(), elements)
        annotated.save(save_path)

        # Encode annotated image
        buffer = io.BytesIO()
        annotated.save(buffer, format="PNG")
        img_base64 = base64.b64encode(buffer.getvalue()).decode()

        return img_base64, elements

    def build_prompt(self, instruction, step, elements):
        """Build action prompt with element list"""
        history_str = ""
        if self.history:
            for h in self.history[-5:]:
                history_str += f"Step {h['step']}: {h['action']}({h['params']}) - {h['reason']}\n"
        else:
            history_str = "No actions executed yet"

        elements_text = elements_to_text(elements)

        return f"""Screen: {self.screen_width}x{self.screen_height} | Step: {step + 1}

Task: {instruction}

Previous actions:
{history_str}

UI Elements on screen:
{elements_text}

Available actions:
1. click - Click element by index. Params: {{"index": number}}
2. long_press - Long press element. Params: {{"index": number}}
3. input - Input text (click input field first). Params: {{"text": "text"}}
4. scroll - Scroll screen. Params: {{"direction": "up/down/left/right"}}
5. back - Go back. Params: {{}}
6. finish - Task complete. Params: {{"answer": "answer"}}
   Query tasks ("View", "Tell me", "Count"): fill in the answer
   Action tasks ("Play", "Add", "Follow"): answer can be empty string

Output ONLY a JSON object for the next action."""

    def execute_instruction(self, instruction, max_steps=15):
        """Execute instruction with SoM-based interaction"""
        logging.info(f"Starting: {instruction}")

        screenshots = []
        self.history = []

        for step in range(max_steps):
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S_%f")
            screenshot_path = os.path.join(self.screenshots_dir, f"step_{step}_{timestamp}.png")

            # Take screenshot and parse UI
            img_base64, elements = self.take_screenshot_and_annotate(screenshot_path)
            screenshots.append(screenshot_path)

            # Build prompt
            user_prompt = self.build_prompt(instruction, step, elements)

            try:
                response = self.client.chat.completions.create(
                    model=self.model_name,
                    messages=[
                        {"role": "system", "content": SYSTEM_PROMPT},
                        {
                            "role": "user",
                            "content": [
                                {"type": "text", "text": user_prompt},
                                {"type": "image_url", "image_url": {"url": f"data:image/png;base64,{img_base64}"}}
                            ]
                        }
                    ],
                    temperature=0.0,
                )

                llm_output = response.choices[0].message.content.strip()
                logging.debug(f"LLM output: {llm_output}")

                # Parse JSON
                json_str = llm_output
                if "```json" in json_str:
                    json_str = json_str.split("```json")[1].split("```")[0].strip()
                elif "```" in json_str:
                    json_str = json_str.split("```")[1].split("```")[0].strip()
                if not json_str.startswith("{"):
                    start = json_str.find("{")
                    end = json_str.rfind("}") + 1
                    if start >= 0 and end > start:
                        json_str = json_str[start:end]

                action_data = json.loads(json_str)
                action_type = action_data.get("action", "")
                params = action_data.get("params", {})
                reason = action_data.get("reason", "")

                logging.info(f"Step {step + 1}: {action_type} {params} - {reason}")

                # Execute action
                if action_type == "click":
                    idx = params.get("index")
                    if idx is not None and 0 <= idx < len(elements):
                        cx, cy = elements[idx].center
                        logging.info(f"  -> clicking element {idx} at ({cx}, {cy})")
                        self.device.click(cx, cy)
                    elif "x" in params and "y" in params:
                        # Fallback: raw coordinates
                        x, y = int(params["x"]), int(params["y"])
                        self.device.click(x, y)
                    time.sleep(1.5)

                elif action_type == "long_press":
                    idx = params.get("index")
                    if idx is not None and 0 <= idx < len(elements):
                        cx, cy = elements[idx].center
                        self.device.long_click(cx, cy, duration=1.0)
                    time.sleep(1.5)

                elif action_type == "input":
                    text = params.get("text", "")
                    self.device.send_keys(text)
                    time.sleep(1)

                elif action_type == "scroll":
                    direction = params.get("direction", "down")
                    cx = self.screen_width // 2
                    cy = self.screen_height // 2
                    dist = self.screen_height // 3
                    if direction == "down":
                        self.device.swipe(cx, cy + dist, cx, cy - dist, duration=0.5)
                    elif direction == "up":
                        self.device.swipe(cx, cy - dist, cx, cy + dist, duration=0.5)
                    elif direction == "left":
                        self.device.swipe(cx + dist, cy, cx - dist, cy, duration=0.5)
                    elif direction == "right":
                        self.device.swipe(cx - dist, cy, cx + dist, cy, duration=0.5)
                    time.sleep(1.5)

                elif action_type == "back":
                    self.device.press("back")
                    time.sleep(1)

                elif action_type == "finish":
                    answer = params.get("answer", "")
                    logging.info(f"Task completed, answer: {answer}")
                    return {
                        "success": True,
                        "steps": step + 1,
                        "actions": self.history,
                        "screenshots": screenshots,
                        "answer": answer,
                        "final_message": answer
                    }

                self.history.append({
                    "step": step + 1,
                    "action": action_type,
                    "params": params,
                    "reason": reason
                })

            except json.JSONDecodeError as e:
                logging.error(f"Step {step + 1} JSON parse failed: {e}")
                logging.error(f"LLM output: {llm_output}")
                self.history.append({
                    "step": step + 1,
                    "action": "parse_error",
                    "params": {},
                    "reason": f"JSON parse failed: {str(e)}"
                })

            except Exception as e:
                logging.error(f"Step {step + 1} failed: {e}")
                return {
                    "success": False,
                    "steps": step + 1,
                    "actions": self.history,
                    "screenshots": screenshots,
                    "error": str(e)
                }

        logging.warning(f"Reached max steps {max_steps}")
        return {
            "success": False,
            "steps": max_steps,
            "actions": self.history,
            "screenshots": screenshots,
            "error": "Reached max steps"
        }

    def close(self):
        pass


def reset_app(device_id, package_name):
    """Reset app"""
    logging.info("Resetting app...")
    subprocess.run(["adb", "-s", device_id, "shell", "am", "force-stop", package_name], capture_output=True)
    subprocess.run(["adb", "-s", device_id, "shell", "pm", "clear", package_name], capture_output=True)
    time.sleep(2)
    subprocess.run(
        ["adb", "-s", device_id, "shell", "monkey", "-p", package_name,
         "-c", "android.intent.category.LAUNCHER", "1"],
        capture_output=True
    )
    time.sleep(5)


def verify_task(task_id, device_id):
    """Verify task result using check scripts"""
    try:
        module_name = f"check_{task_id}"
        mod = __import__(module_name)
        from check_common import AppChecker
        checker = AppChecker(device_id)
        return mod.check(checker)
    except Exception as e:
        logging.error(f"Verification failed: {e}")
        return False


def main():
    parser = argparse.ArgumentParser(description="MySpotify GUI Agent Automation Testing")
    parser.add_argument("--device", "-d", required=True, help="Device ID")
    parser.add_argument("--task", "-t", type=int, help="Task ID (1-40)")
    parser.add_argument("--all", action="store_true", help="Run all tasks")
    parser.add_argument("--start", type=int, default=1, help="Start task ID")
    parser.add_argument("--end", type=int, help="End task ID")
    parser.add_argument("--output", "-o", help="Output file")
    parser.add_argument("--no-reset", action="store_true", help="Don't reset app")
    parser.add_argument("--max-steps", type=int, default=15, help="Max steps per task")
    parser.add_argument("--verbose", "-v", action="store_true", help="Verbose output")
    parser.add_argument("--model", "-m", type=str, default=None,
                        choices=["seed-1.8", "seed-2.0-pro"],
                        help="Model to use: seed-1.8 (doubao-seed-1-8) or seed-2.0-pro (Doubao-Seed-2.0-pro)")

    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s - %(levelname)s - %(message)s"
    )

    api_key = os.getenv("API_KEY")
    base_url = os.getenv("API_BASE")

    # Model selection
    MODEL_MAP = {
        "seed-1.8": os.getenv("SEED_1_8", "ep-20260415155052-4htzp"),
        "seed-2.0-pro": os.getenv("SEED_2_0_PRO", "ep-20260415155605-965gt"),
    }
    if args.model:
        model_name = MODEL_MAP[args.model]
    else:
        model_name = os.getenv("MODEL_NAME", MODEL_MAP["seed-2.0-pro"])

    if not api_key:
        logging.error("Please set API_KEY in .env file")
        sys.exit(1)

    tasks_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tasks.json")
    with open(tasks_file, "r", encoding="utf-8") as f:
        tasks_data = json.load(f)

    app_package = tasks_data["app_package"]
    all_tasks = tasks_data["tasks"]

    if args.task:
        task_list = [t for t in all_tasks if t["task_id"] == args.task]
    elif args.all:
        task_list = all_tasks
    else:
        end_id = args.end or len(all_tasks)
        task_list = [t for t in all_tasks if args.start <= t["task_id"] <= end_id]

    if not task_list:
        logging.error("No tasks to run")
        sys.exit(1)

    screenshots_dir = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        f"screenshots_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
    )
    agent = SimpleAgent(api_key, base_url, model_name, args.device, screenshots_dir)

    logging.info(f"Starting {len(task_list)} tasks")
    logging.info(f"  Model: {model_name}")
    logging.info(f"  Device: {args.device}")
    logging.info(f"  Screen: {agent.screen_width}x{agent.screen_height}")

    output_path = args.output or os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        f"results_{datetime.now().strftime('%Y%m%d_%H%M%S')}.jsonl"
    )

    results = []

    for idx, task in enumerate(task_list, 1):
        task_id = task["task_id"]
        instruction = task["instruction"]

        logging.info(f"\n{'='*60}")
        logging.info(f"[{idx}/{len(task_list)}] Task {task_id}: {instruction}")
        logging.info(f"{'='*60}")

        if not args.no_reset:
            reset_app(args.device, app_package)

        agent.reset()
        result = agent.execute_instruction(instruction, max_steps=args.max_steps)

        if result["success"]:
            verified = verify_task(task_id, args.device)
            logging.info(f"Verification: {'PASS' if verified else 'FAIL'}")
        else:
            verified = False
            logging.error(f"Failed: {result.get('error', 'Unknown')}")

        task_result = {
            "task_id": task_id,
            "instruction": instruction,
            "difficulty": task.get("difficulty", 0),
            "success": result["success"],
            "verified": verified,
            "steps": result["steps"],
            "answer": result.get("answer"),
            "error": result.get("error"),
            "actions": result.get("actions", []),
            "timestamp": datetime.now().isoformat()
        }
        results.append(task_result)

        with open(output_path, "a", encoding="utf-8") as f:
            f.write(json.dumps(task_result, ensure_ascii=False) + "\n")

    # Statistics
    logging.info(f"\n{'='*60}")
    logging.info("Testing completed")
    logging.info(f"{'='*60}")

    total = len(results)
    exec_success = sum(1 for r in results if r["success"])
    verify_pass = sum(1 for r in results if r["verified"])

    logging.info(f"Total:      {total}")
    logging.info(f"Exec OK:    {exec_success}/{total} ({exec_success/total*100:.1f}%)")
    logging.info(f"Verified:   {verify_pass}/{total} ({verify_pass/total*100:.1f}%)")

    for diff in [1, 2, 3]:
        dr = [r for r in results if r["difficulty"] == diff]
        if dr:
            dp = sum(1 for r in dr if r["verified"])
            logging.info(f"  Diff {diff}: {dp}/{len(dr)} ({dp/len(dr)*100:.1f}%)")

    logging.info(f"\nResults: {output_path}")


if __name__ == "__main__":
    main()
