# Tetris Refactoring & Extension (COMP2042)

![Java](https://img.shields.io/badge/Java-17%2B-orange) ![JavaFX](https://img.shields.io/badge/Framework-JavaFX-blue) ![JUnit](https://img.shields.io/badge/Testing-JUnit%205-green) ![Status](https://img.shields.io/badge/Coursework-Completed-brightgreen)

**Student Name:** Muhammad Atif Hakimi  
**Repository:** [GitHub Link](https://github.com/AtifAsrul/CW2025.git)

## 📖 Project Overview
This project is a refactoring and extension of a legacy JavaFX Tetris application. The coursework focuses on adopting **MVC Architecture**, implementing **SOLID principles**, and introducing modern gameplay features such as a 7-Bag Randomizer, Ghost Pieces, and a dynamic "Neo Jazz" audio engine.

---

## 🛠 Prerequisites & Compilation

This project is built using **IntelliJ IDEA** and **Maven**.

### Requirements
* **Java SDK:** Version 17 or higher (Must be compatible with JavaFX).
* **Maven:** Required for dependency management (ensures `javafx-media` and `junit` are loaded).
* **JUnit 5:** Required for the test suite.

### 🚀 How to Run
1. Open the project in **IntelliJ IDEA**.
2. Navigate to `src/main/java/com/comp2042/Launcher.java`.
3. Right-click `Launcher.java` and select **Run 'Launcher.main()'**.

### 🧪 How to Run Tests
1. Navigate to `src/test/java/`.
2. Right-click on `MatrixOperationsTest.java`.
3. Select **Run 'MatrixOperationsTest'**.
4. *Verify that all unit tests pass (green).*

---

## 🎮 Controls

| Action | Key Binding |
| :--- | :--- |
| **Move Left/Right** | `Left Arrow` / `Right Arrow` |
| **Rotate** | `Up Arrow` |
| **Soft Drop** | `Down Arrow` |
| **Hard Drop** | `SPACE` |
| **Hold Piece** | `C` or `Shift` |
| **Pause Game** | `P` |

---

## 💎 Features & Implementation

### A. Refactoring & Maintenance (35%)
The core focus was stabilizing the legacy codebase and enforcing software engineering standards.

* **MVC Architecture Separation:**
    * Extracted grid logic from `SimpleBoard` into a new class `MatrixOperations`.
    * **Benefit:** Enforces the **Single Responsibility Principle (SRP)**. `SimpleBoard` manages state, while `MatrixOperations` handles low-level array manipulation.
* **Removal of Magic Numbers:**
    * Replaced hardcoded integers (e.g., board width 10, height 20) with named constants in `Constants.java`.
* **Robust Unit Testing:**
    * Created `MatrixOperationsTest` using **JUnit 5**.
    * Tests cover: Boundary checks, Collision detection, and Line clearing logic.
* **Score Logic Fix:**
    * Fixed a bug where merely moving a piece increased the score. Points are now strictly awarded for clearing lines.
* **Standardized Gameplay:**
    * `START_Y` set to 0. Pieces now spawn correctly at the top of the board.

### B. Extensions & New Features (25%)

#### 1. "Neo Jazz" Sound & Music System 🎷
A procedural audio engine built with `javafx-media`.
* **Adaptive Chords:** Rewrote the audio engine to play specific jazz chords based on lines cleared:
    * **1 Line:** `Cm9` (Electric Piano - Smooth)
    * **2 Lines:** `Fm9` (Soulful Electric Piano)
    * **3 Lines:** `G7#9` ("Hendrix Chord" - Tension)
    * **Tetris:** `CmM7` (Loud Brass Finale)
* **Audio Mixing:** Background music (`SwingBGM.mp3`) loops seamlessly. Sound effect volumes (Hard Drops) were rebalanced (127 → 45) to sit perfectly in the mix.

#### 2. Gameplay Mechanics
* **Ghost Piece (Shadow):** Renders a semi-transparent shadow showing exactly where the piece will land.
* **Hard Drop:** Pressing `SPACE` performs an atomic, instant drop to the lowest valid position.
* **Hold Piece:** Press `C` or `Shift` to swap the current piece for a held piece.
* **Modern 7-Bag Randomizer:** Implemented SRS-compliant randomization. Generates a shuffled "bag" of 7 unique tetrominoes to ensure fair distribution.

#### 3. Visual Overhaul (Vaporwave/Neon Theme) 🎨
* **UI Polish:** "Let's go Digital" font, Dark Dimmed Overlays (85% opacity) for Pause/Game Over screens.
* **Color Palette:** High-contrast Neon Cyan, Purple, and Green bricks against a dark background.
* **Grid:** "Super Dim" grid (5% opacity) for reduced visual clutter.

#### 4. Adaptive Progression
* **5-Level System:** Speed increases at score thresholds (0, 300, 800, 1500, 3000).
* **UI:** Displays "∞" instead of "Level 5" when max speed is reached.

---

## 📂 Project Structure

### New Classes
| Class Name | Purpose |
| :--- | :--- |
| `LevelConfig.java` | Configuration logic defining score thresholds and game speeds. |
| `MatrixOperations.java` | **Logic Layer.** Handles 2D array logic, collision, and line clearing. |
| `MatrixOperationsTest.java` | **Test Suite.** JUnit 5 tests for core mechanics. |
| `RandomBrickGenerator.java` | Implements the **7-Bag Randomizer** system. |

### Modified Classes
| Class Name | Key Modifications |
| :--- | :--- |
| `SoundManager.java` | Added `MediaPlayer` for BGM, chord logic, and volume rebalancing. |
| `GameController.java` | Added lifecycle management (`stopGame`), `hold` logic, and Hard Drop. |
| `GuiController.java` | Fixed dimmer toggles (85% opacity), refined brick rendering. |
| `ViewData.java` | Added `ghostY` field for rendering the shadow piece. |
| `SimpleBoard.java` | Implemented `calculateGhostY()` and 7-Bag spawning integration. |

---

## 🐛 Known Issues & Solutions

### Implemented but Not Working Perfectly
* **Next Piece Display Jitter:** The "Next Piece" preview box functions logically, but visually causes a slight border shift when switching between wide (I-Piece) and narrow (O-Piece) shapes due to JavaFX `GridPane` dynamic resizing.

### Unexpected Problems & Solutions
> **The "Wobbly" UI Layout**
> * **Problem:** The main game board border would expand/contract based on the active piece shape.
> * **Solution:** Implemented a **Fixed Window Layout** (700px height, non-resizable) and masked the shift using dark transparent UI panels to make the layout feel intentional.

> **Legacy Collision Bugs**
> * **Problem:** Original code relied on magic numbers, making board resizing impossible.
> * **Solution:** Isolated math logic into `MatrixOperations` and wrote Unit Tests to verify collision logic before touching the GUI.
