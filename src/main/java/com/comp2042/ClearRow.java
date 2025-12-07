package com.comp2042;

/**
 * Data class representing the result of a row clearing operation.
 * Contains information about lines removed, the new board state, and score.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final java.util.List<Integer> clearedRows;

    /**
     * Constructs a new ClearRow object.
     *
     * @param linesRemoved the number of lines removed.
     * @param newMatrix    the new state of the board matrix.
     * @param scoreBonus   the score bonus earned.
     * @param clearedRows  list of indices of the cleared rows.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, java.util.List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows;
    }

    /**
     * Gets the number of lines removed.
     *
     * @return the count of removed lines.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the new board matrix after clearing rows.
     *
     * @return a copy of the new board matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus for this clear operation.
     *
     * @return the score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Gets the list of indices of the cleared rows.
     *
     * @return the list of row indices.
     */
    public java.util.List<Integer> getClearedRows() {
        return clearedRows;
    }
}
