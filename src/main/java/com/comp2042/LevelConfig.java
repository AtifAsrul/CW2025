package com.comp2042;

public class LevelConfig {

    // Score thresholds for each level (1-5)
    // Level 1 starts at 0
    // Level 2 starts at 300
    // Level 3 starts at 800
    // Level 4 starts at 1500
    // Level 5 starts at 3000
    private static final int[] LEVEL_THRESHOLDS = { 0, 300, 800, 1500, 3000 };

    // Milliseconds delay for each level (lower is faster)
    private static final double[] LEVEL_SPEEDS = { 400, 320, 240, 160, 80 };

    /**
     * Calculates the current level based on the score.
     * 
     * @param score Current game score.
     * @return The level (1-based index).
     */
    public static int getLevelForScore(int score) {
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (score >= LEVEL_THRESHOLDS[i]) {
                return i + 1;
            }
        }
        return 1;
    }

    /**
     * Gets the game speed (delay in ms) for a specific level.
     * 
     * @param level The current level (1-5).
     * @return The delay in milliseconds.
     */
    public static double getSpeedForLevel(int level) {
        // Clamp level to valid range
        int safeLevel = Math.max(1, Math.min(level, LEVEL_SPEEDS.length));
        return LEVEL_SPEEDS[safeLevel - 1];
    }
}
