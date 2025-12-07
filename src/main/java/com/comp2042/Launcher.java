package com.comp2042;

/**
 * Launcher class to workaround JavaFX 11+ modules issues.
 * Delegates the main execution to {@link Main}.
 */
public class Launcher {
    /**
     * Main method that calls {@link Main#main(String[])}.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
