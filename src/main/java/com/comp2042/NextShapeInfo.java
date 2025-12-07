package com.comp2042;

/**
 * Data class representing information about the next rotation state of a brick.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a new NextShapeInfo object.
     *
     * @param shape    the matrix of the next shape.
     * @param position the rotation index of the next shape.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets the shape matrix.
     *
     * @return a copy of the shape matrix.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the position (rotation index) of the shape.
     *
     * @return the position index.
     */
    public int getPosition() {
        return position;
    }
}
