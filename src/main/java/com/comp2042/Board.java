package com.comp2042;

/**
 * Interface defining the contract for the game board logic.
 * Handles brick movement, rotation, spawning, and state management.
 */
public interface Board {

    /**
     * Attempts to move the current brick down.
     *
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current brick to the left.
     *
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current brick to the right.
     *
     * @return true if the move was successful, false otherwise.
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current brick to the left.
     *
     * @return true if the rotation was successful, false otherwise.
     */
    boolean rotateLeftBrick();

    /**
     * Swaps the current brick with the held brick.
     *
     * @return true if the hold action was successful, false if already held this
     *         turn.
     */
    boolean holdBrick();

    /**
     * Spawns a new brick.
     *
     * @return true if the new brick causes a collision (Game Over), false
     *         otherwise.
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the board matrix.
     *
     * @return a 2D array representing the board.
     */
    int[][] getBoardMatrix();

    /**
     * Gets the view data for the current state (brick position, ghost, etc.).
     *
     * @return the ViewData object.
     */
    ViewData getViewData();

    /**
     * Locks the current brick to the board.
     */
    void lockBrickToBoard();

    /**
     * Checks and clears completed rows.
     *
     * @return a ClearRow object containing details of cleared rows.
     */
    ClearRow clearRows();

    /**
     * Gets the score object.
     *
     * @return the Score object.
     */
    Score getScore();

    /**
     * Resets the board for a new game.
     */
    void newGame();
}
