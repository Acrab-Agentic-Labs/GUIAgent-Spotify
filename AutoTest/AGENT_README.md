# GUI Agent 自动化测试

使用多模态LLM自动执行MySpotify应用的测试任务。

## 配置

1. 安装依赖:
```bash
pip install openai python-dotenv uiautomator2 pillow
```

2. 配置API:
复制 `.env.example` 为 `.env`，填入你的API配置:
```
API_KEY=your_api_key_here
API_BASE=https://api.openai.com/v1
MODEL_NAME=gpt-4o
```

3. 连接Android设备:
```bash
adb devices
```

## 使用方法

### 运行单个任务
```bash
python agent_runner.py --device emulator-5554 --task 1
```

### 运行所有任务
```bash
python agent_runner.py --device emulator-5554 --all
```

### 运行指定范围的任务
```bash
python agent_runner.py --device emulator-5554 --start 1 --end 10
```

### 保存结果到文件
```bash
python agent_runner.py --device emulator-5554 --all --output results.jsonl
```

### 不重置应用状态
```bash
python agent_runner.py --device emulator-5554 --task 1 --no-reset
```

### 详细输出模式
```bash
python agent_runner.py --device emulator-5554 --task 1 --verbose
```

## 参数说明

- `--device, -d`: Android设备ID (必需)
- `--task, -t`: 运行单个任务ID (1-40)
- `--all`: 运行所有任务
- `--start`: 起始任务ID (默认: 1)
- `--end`: 结束任务ID (默认: 最后一个)
- `--output, -o`: 结果输出文件 (JSONL格式)
- `--screenshots-dir`: 截图保存目录 (默认: ./screenshots)
- `--no-reset`: 不重置应用状态
- `--verbose, -v`: 详细输出模式
- `--max-steps`: 每个任务最大步骤数 (默认: 10)

## 工作原理

1. Agent读取任务指令
2. 截取当前屏幕
3. 使用多模态LLM分析屏幕并决定下一步操作
4. 执行操作 (点击、输入、滚动等)
5. 重复步骤2-4直到任务完成
6. 使用check脚本验证结果

## 输出格��

结果以JSONL格式保存，每行一个任务:
```json
{
  "task_id": 1,
  "instruction": "查看当前用户名称",
  "agent_result": {
    "success": true,
    "steps": 3,
    "answer": "User"
  },
  "verify_result": {
    "success": true,
    "message": "验证通过"
  }
}
```

## 注意事项

- 确保Android设备已连接并启用USB调试
- 确保应用已安装: com.example.myspotify
- 每个任务前会自动重置应用状态 (除非使用 --no-reset)
- 截图会保存在指定目录，可用于调试
