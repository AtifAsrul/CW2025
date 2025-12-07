COMP2042 - Coursework: Tetris Refactoring & Extension

Student Name: Muhammad Atif Hakimi
GitHub Repository: https://github.com/AtifAsrul/CW2025.git

1. Compilation Instructions

This project is a JavaFX application built using IntelliJ IDEA and Maven (or standard Java SDK depending on your setup).

Prerequisites

Java SDK: Version 17 or higher (compatible with JavaFX).

Maven: Ensuring pom.xml dependencies are loaded.

Note: Added javafx-media dependency for background music playback (MP3).

JUnit 5: Required for running the test suite.

How to Run

Open the project in IntelliJ IDEA.

Navigate to src/main/java/com/comp2042/Launcher.java.

Right-click Launcher.java and select Run 'Launcher.main()'.

The game window should appear.

How to Run Tests

Navigate to src/test/java/.

Right-click on MatrixOperationsTest.java.

Select Run 'MatrixOperationsTest'.

Verify that all unit tests pass (green).

2. Implemented and Working Properly

A. Refactoring & Maintenance (35% Component)

MVC Architecture Separation:

Extracted grid logic from SimpleBoard into a new class MatrixOperations. This enforces the Single Responsibility Principle (SRP).

SimpleBoard now acts as a high-level manager for game state, while MatrixOperations handles low-level array manipulation.

Magic Numbers Removed: Replaced hardcoded integer values (e.g., board width 10, height 20) with named constants for better maintainability.

Unit Testing: Implemented a robust JUnit 5 test suite (MatrixOperationsTest) covering:

Boundary checks (preventing pieces from moving off-screen).

Collision detection (preventing overlapping blocks).

Line clearing logic.

Score Logic Fix: Removed a bug where the score would increase by 1 just for moving a piece down manually. Now, points are strictly awarded for clearing lines.

Standardized Gameplay: Updated Constants.java to set START_Y to 0. Pieces now spawn at the top of the board (standard Tetris behavior).

B. New Features / Extensions (25% Component)

5-Level Adaptive Progression System:

Description: Implemented a dynamic difficulty system where the game speed increases as the player earns points.

Logic: Levels advance based on score thresholds (0, 300, 800, 1500, 3000). The speed increases at each stage to challenge the player.

UI Integration: Added a dedicated "Level" display box. Visual Polish: Configured the display to show "∞" instead of "Level 5" when the final threshold is reached.

Hold Piece Mechanism:

Description: Players can press 'C' or 'Shift' to hold the current piece for later use. Swapping pieces is allowed once per turn.

Logic: Implemented holdBrick() within SimpleBoard and handled via InputEventListener/GameController.

"Neo Jazz" Sound & Music System:

Background Music (BGM): Integrated SwingBGM.mp3 using javafx-media. Music loops seamlessly during gameplay and features smart lifecycle management (stops on "Game Over" or "Menu", restarts on "New Game").

Procedural Jazz Chords: Rewrote the audio engine to play specific chords based on lines cleared:

1 Line: Cm9 (Electric Piano) - Smooth and relaxed.

2 Lines: Fm9 (Soulful Electric Piano).

3 Lines: G7#9 ("Hendrix Chord") - Building tension.

Tetris (4 Lines): CmM7 (Loud Brass Finale).

Audio Balancing: Drastically reduced volume for high-velocity sounds (Tetris Clear: 127→45, Hard Drop: 110→50) to mix perfectly with the background jazz.

Visual Overhaul & UI/UX Improvements:

Main Menu Aesthetics: Overhauled the menu with a Vaporwave/Retro background (new_main_menu_bg.jpg) and "Let's go Digital" font. Enhanced the title with a thick 4px stroke and deep purple glow.

Smart Overlays: Implemented a Dark Dimmed Overlay (85% opacity) that activates correctly for both "Pause" and "Game Over" states.

High-Contrast Neon Palette: Updated ColorHelper.java to use vibrant Cyan, Neon Purple, and Green to make active pieces pop against the dark background.

Crisp Brick Styling: Sharpened brick appearance (Radius 9 → 5) and added solid black borders.

Grid Design: Implemented a "Super Dim" grid (White, 5% opacity) to reduce visual clutter.

Integrated Tutorial: Added a permanent "Controls" overlay box to the gameplay screen.

Ghost Piece (Shadow):

Description: A semi-transparent "shadow" of the active piece is rendered at the bottom of the board, showing exactly where the piece will land if dropped.

Benefit: Massively increases precision and allows players to use the Hard Drop feature with confidence.

Hard Drop Mechanism:

Description: Pressing SPACE instantly drops the active piece to the lowest valid position and locks it.

Implementation: Calculates the maximum drop distance in one atomic step (0 frames) in GameController.

Modern 7-Bag Randomizer (SRS Compliance):

Description: Replaced the legacy Random.nextInt() generation with a "7-Bag" system.

Logic: The game generates a shuffled "bag" of all 7 tetrominoes. Pieces are drawn from this bag until empty, then a new bag is generated.

Fixed Window Layout:

Window Resizing: Increased WINDOW_HEIGHT to 700px and disabled window resizing (setResizable(false)). This ensures UI elements are never obstructed.

3. Implemented but Not Working Properly

Next Piece Display Layout:

The "Next Piece" preview box functions logic-wise, but visually causes the main game border to shift slightly when the piece shape changes (e.g., from an 'I' to an 'O'). This is due to the dynamic resizing of JavaFX containers.

4. New Java Classes

Class Name

Purpose

LevelConfig.java

Configuration/Logic. Defines the score thresholds (0, 300, 800, etc.) and corresponding game speeds for the progression system.

MatrixOperations.java

Logic Layer. Handles the 2D array logic for the board, including collision detection, checking bounds, and clearing full lines. Separates math from game state.

MatrixOperationsTest.java

Test Suite. Contains JUnit 5 tests to verify the core mechanics (collision and clearing) work without requiring the GUI to be running.

RandomBrickGenerator.java

Logic Layer. Implements the 7-Bag Randomizer system to ensure fair distribution of pieces.

5. Modified Java Classes

Class Name

Modifications Made

SoundManager.java

Audio Engine: Added MediaPlayer for BGM and stopBackgroundMusic(). Implemented Neo Jazz chord logic. Rebalanced volumes (Velocity 127→45) to prevent ear fatigue.

GameController.java

Lifecycle: Integrated stopGame() logic to clean up audio/BGM when returning to menu. Added soundManager.playHold() and input mapping for SPACE (Hard Drop).

GuiController.java

UI/UX: Fixed toggleDimmer logic to handle 85% opacity overlays for Pause/Game Over. Updated backToMenu to ensure proper game shutdown. Refined brick rendering logic.

MainMenuController.java

Visuals: Added logic to load the custom "digital.ttf" font dynamically on startup.

InputEventListener.java

Interface: Added void stopGame() method to the interface to allow proper cleanup commands between controllers.

ColorHelper.java

Updated the brick color palette to High-Contrast Neon (Cyan, Neon Purple, Neon Green) to improve visibility.

ViewData.java

Ghost Piece Logic: Added ghostY field to store the calculated shadow coordinate. Updated constructor to accept this value.

SimpleBoard.java

Core Logic: Added calculateGhostY() to simulate drops. Implemented 7-Bag spawning and holdBrick() logic.

6. Unexpected Problems & Solutions

Issue 1: The "Wobbly" UI Layout

Problem: During the implementation of the "Next Piece" display, the main game board border would expand and contract by a few pixels whenever the active piece changed.
Cause: The JavaFX GridPane defaults to auto-sizing based on content. When a wide piece (I-Block) spawned, the column expanded, shifting the entire UI.
Solution: I focused on stabilizing the window size and background layers instead. While the grid still shifts slightly, the new Fixed Window Layout, Fixed Width Side Panels, and Dark Transparent UI Panels mask the issue, making the experience feel intentional rather than buggy.

Issue 2: Legacy Collision Bugs

Problem: The original code had magic numbers scattered across multiple files, making it difficult to change the board size or spawn point without crashing the game.
Solution: I introduced MatrixOperations as a dedicated utility class. This allowed me to isolate the math errors and write Unit Tests to prove the collision logic was correct before running the GUI.
