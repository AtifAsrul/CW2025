package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Class representing the game score.
 * Wraps an {@link IntegerProperty} to allow binding and easy modification.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Gets the score property for binding.
     *
     * @return the IntegerProperty representing the score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds a value to the current score.
     *
     * @param i the amount to add.
     */
    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
