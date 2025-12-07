package com.comp2042;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Helper class for mapping integer IDs to JavaFX Paint objects.
 * Used for defining the colors of the bricks.
 */
public class ColorHelper {

    /**
     * Retrieves the paint color corresponding to a given index.
     *
     * @param i the color index (0 for transparent, 1-7 for specific colors).
     * @return the corresponding Paint object.
     */
    public static Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.web("#00FFFF"); // Neon Cyan
                break;
            case 2:
                returnPaint = Color.web("#D500F9"); // Neon Purple
                break;
            case 3:
                returnPaint = Color.web("#39FF14"); // Neon Green
                break;
            case 4:
                returnPaint = Color.web("#FFFF00"); // Neon Yellow
                break;
            case 5:
                returnPaint = Color.web("#FF00FF"); // Neon Magenta
                break;
            case 6:
                returnPaint = Color.web("#FF073A"); // Neon Red
                break;
            case 7:
                returnPaint = Color.web("#FF5F00"); // Neon Orange
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }
}
