package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick (tetromino).
 * Provides the matrices for different rotations of the brick.
 */
public interface Brick {

    /**
     * Gets the list of shape matrices representing rotations.
     *
     * @return a List of 2D integer arrays, each representing a rotation state.
     */
    List<int[][]> getShapeMatrix();
}
