package com.comp2042.logic.bricks;

/**
 * Interface for generating bricks.
 * Supports retrieving the current and next brick.
 */
public interface BrickGenerator {

    /**
     * Gets the next brick from the generator.
     *
     * @return the next Brick object.
     */
    Brick getBrick();

    /**
     * Peeks at the next brick without removing it from the queue.
     *
     * @return the next Brick object.
     */
    Brick getNextBrick();
}
