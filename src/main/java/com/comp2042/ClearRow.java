package com.comp2042;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final java.util.List<Integer> clearedRows;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, java.util.List<Integer> clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public java.util.List<Integer> getClearedRows() {
        return clearedRows;
    }
}
