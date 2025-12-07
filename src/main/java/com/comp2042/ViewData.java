package com.comp2042;

/**
 * Data class containing all necessary information to render the game view.
 * Includes current brick, position, ghost position, next brick, and held brick.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostY;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;

    /**
     * Constructs a new ViewData object.
     *
     * @param brickData     the matrix of the current falling brick.
     * @param xPosition     the x coordinate of the brick.
     * @param yPosition     the y coordinate of the brick.
     * @param ghostY        the y coordinate of the ghost brick.
     * @param nextBrickData the matrix of the next upcoming brick.
     * @param heldBrickData the matrix of the currently held brick.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostY, int[][] nextBrickData,
            int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostY = ghostY;
        this.nextBrickData = nextBrickData;
        this.heldBrickData = heldBrickData;
    }

    /**
     * Gets the matrix data of the current brick.
     *
     * @return a copy of the brick matrix.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x position of the brick.
     *
     * @return the x coordinate.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y position of the brick.
     *
     * @return the y coordinate.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the y position of the ghost brick.
     *
     * @return the ghost y coordinate.
     */
    public int getGhostY() {
        return ghostY;
    }

    /**
     * Gets the matrix data of the next brick.
     *
     * @return a copy of the next brick matrix.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the matrix data of the held brick.
     *
     * @return a copy of the held brick matrix, or null if none is held.
     */
    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
}
