package com.comp2042;

import com.comp2042.logic.bricks.Brick;

/**
 * Logic class responsible for handling brick rotation.
 * Manages the current rotation state of a brick.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * previews the next rotation shape without applying it.
     *
     * @return a NextShapeInfo object containing the next shape matrix and its
     *         rotation index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the matrix of the current shape.
     *
     * @return the 2D array representing the current rotation of the brick.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current shape orientation index.
     *
     * @param currentShape the index of the rotation state.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to be managed by this rotator and resets rotation to 0.
     *
     * @param brick the brick to rotate.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Gets the current brick being managed.
     *
     * @return the Brick object.
     */
    public Brick getBrick() {
        return brick;
    }

}
