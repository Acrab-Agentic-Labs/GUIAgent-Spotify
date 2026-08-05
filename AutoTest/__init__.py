# -*- coding:utf-8 -*-

import sys
import os
import importlib

from appsim.tasks.base import TaskItem, AppTasks

# Add this directory to sys.path so check scripts can use
# `from check_common import AppChecker` without modification
_dir = os.path.dirname(os.path.abspath(__file__))
if _dir not in sys.path:
    sys.path.insert(0, _dir)

from check_common import AppChecker


def _make_verify(n):
    """Create an AppSim-compatible verify_func from check_N.check()."""

    def verify_func(result=None, device_id=None, backup_dir=None):
        mod = importlib.import_module(f"check_{n}")
        checker = AppChecker(serial=device_id)
        return mod.check(checker)

    return verify_func


SPOTIFY_TASKS = AppTasks(
    package_name="com.example.myspotify",
    task_items=[
        # ---- Info (is_reasoning=True) ----
        TaskItem(
            instruction="View the current user name",
            verify_func=_make_verify(1),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Name of the currently playing song",
            verify_func=_make_verify(2),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Count the number of categories on the search page",
            verify_func=_make_verify(3),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction='Count the number of songs in the first recommended playlist under "to get you started"',
            verify_func=_make_verify(4),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Tell me the title of the first podcast on the podcast page",
            verify_func=_make_verify(5),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Tell me the release time of the first podcast",
            verify_func=_make_verify(6),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Tell me the name of the first audiobook",
            verify_func=_make_verify(7),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Tell me the playing duration of the first audiobook",
            verify_func=_make_verify(8),
            human_steps=3,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="Tell me who the singer of the currently playing song is",
            verify_func=_make_verify(9),
            human_steps=3,
            is_reasoning=True,
        ),
        # ---- Playback (is_reasoning=False) ----
        TaskItem(
            instruction="Play or pause the song on the full-screen playback page",
            verify_func=_make_verify(10),
            human_steps=3,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Play the next song",
            verify_func=_make_verify(11),
            human_steps=3,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Enable shuffle play mode",
            verify_func=_make_verify(12),
            human_steps=3,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Enable repeat single song mode",
            verify_func=_make_verify(13),
            human_steps=3,
            is_reasoning=False,
        ),
        # ---- Library (is_reasoning=False) ----
        TaskItem(
            instruction="Add the currently playing song to favorites",
            verify_func=_make_verify(14),
            human_steps=3,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Remove the currently playing song from favorites",
            verify_func=_make_verify(15),
            human_steps=3,
            is_reasoning=False,
        ),
        # ---- Playback (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction="Set the timer to 15 minutes",
            verify_func=_make_verify(16),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Turn off the timer",
            verify_func=_make_verify(17),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Info (is_reasoning=True, difficulty=2) ----
        TaskItem(
            instruction="View the lyrics of the currently playing song",
            verify_func=_make_verify(18),
            human_steps=7,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="View the production information of the current song",
            verify_func=_make_verify(19),
            human_steps=7,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="View the artist information of the current song",
            verify_func=_make_verify(20),
            human_steps=7,
            is_reasoning=True,
        ),
        # ---- Search (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction='Search for the song "Shape of You"',
            verify_func=_make_verify(21),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="View the content of the 'Music' category on the search page",
            verify_func=_make_verify(22),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Open the QR code scanning page",
            verify_func=_make_verify(23),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Delete one recent search record on the search input page",
            verify_func=_make_verify(24),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Playlist (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction="Create a new playlist named 'My Rock' in the music library",
            verify_func=_make_verify(25),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Library (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction="Open the Liked Songs playlist",
            verify_func=_make_verify(26),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Open Taylor Swift's homepage in the music library",
            verify_func=_make_verify(27),
            human_steps=7,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Follow a new artist",
            verify_func=_make_verify(28),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Podcast (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction="Follow a new podcast",
            verify_func=_make_verify(29),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Playlist (is_reasoning=False, difficulty=2) ----
        TaskItem(
            instruction="Randomly add a playlist from the homepage to the music library",
            verify_func=_make_verify(30),
            human_steps=7,
            is_reasoning=False,
        ),
        # ---- Library (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Add a song to Liked Songs",
            verify_func=_make_verify(31),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Playlist (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Add a song to any playlist in the music library except the Liked Songs playlist",
            verify_func=_make_verify(32),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Premium (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Perform a membership payment operation",
            verify_func=_make_verify(33),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Podcast (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Post the comment 'Great episode!' on a random podcast",
            verify_func=_make_verify(34),
            human_steps=12,
            is_reasoning=False,
        ),
        TaskItem(
            instruction="Fast forward a certain podcast by 15 seconds",
            verify_func=_make_verify(35),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Library (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Unfollow Taylor Swift",
            verify_func=_make_verify(36),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Audiobook (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Randomly add an audiobook to favorites",
            verify_func=_make_verify(37),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Playlist (is_reasoning=False, difficulty=3) ----
        TaskItem(
            instruction="Create a new playlist and add two new songs to it",
            verify_func=_make_verify(38),
            human_steps=12,
            is_reasoning=False,
        ),
        # ---- Info (is_reasoning=True, difficulty=3) ----
        TaskItem(
            instruction="Among the followed singers, who has the most works",
            verify_func=_make_verify(39),
            human_steps=12,
            is_reasoning=True,
        ),
        TaskItem(
            instruction="How many liked songs are there in the music library currently",
            verify_func=_make_verify(40),
            human_steps=12,
            is_reasoning=True,
        ),
    ],
)
