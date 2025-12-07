package com.comp2042;

/**
 * Controller class that manages the core game logic and interaction between the
 * model (Board) and the view (GuiController).
 * implements {@link InputEventListener} to handle user input events.
 */
public class GameController implements InputEventListener {

    private Board board = new SimpleBoard();
    private final GuiController viewGuiController;
    private final SoundManager soundManager;

    /**
     * Constructs a new GameController.
     * Initializes the sound manager, creates a new brick, and sets up the view.
     *
     * @param c the GuiController instance to control the view.
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        soundManager = new SoundManager();
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        soundManager.playBackgroundMusic(); // Start BGM
    }

    /**
     * Handles the event when the down key is pressed or time tick occurs.
     * Moves the brick down. If the brick cannot move down, it locks it to the
     * board, checks for cleared rows,
     * updates the score, and spawns a new brick.
     *
     * @param event the move event details.
     * @return the data representing the state after the down move, including
     *         cleared rows and view data.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.lockBrickToBoard();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                soundManager.playLineClear(clearRow.getLinesRemoved());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
                soundManager.playGameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the event when the left key is pressed.
     * Moves the brick to the left.
     *
     * @param event the move event details.
     * @return the updated view data after the move.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the event when the right key is pressed.
     * Moves the brick to the right.
     *
     * @param event the move event details.
     * @return the updated view data after the move.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the event when the rotate key is pressed.
     * Rotates the brick.
     *
     * @param event the move event details.
     * @return the updated view data after the rotation.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Handles the event when the hold key is pressed.
     * Swaps the current brick with the held brick.
     *
     * @param event the move event details.
     * @return the updated view data after the hold action.
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        if (board.holdBrick()) {
            soundManager.playHold();
        }
        return board.getViewData();
    }

    /**
     * Handles the event when the drop (space) key is pressed.
     * Instantly drops the brick to the bottom, locks it, processes row clears, and
     * spawns a new brick.
     *
     * @param event the move event details.
     * @return the data representing the state after the drop, including cleared
     *         rows and view data.
     */
    @Override
    public DownData onDropEvent(MoveEvent event) {
        boolean canMove = true;
        while (canMove) {
            canMove = board.moveBrickDown();
        }

        int column = board.getViewData().getxPosition();
        soundManager.playHardDrop(column);

        board.lockBrickToBoard();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            soundManager.playLineClear(clearRow.getLinesRemoved());
        }
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
            soundManager.playGameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Starts a new game by resetting the board and playing the background music.
     */
    @Override
    public void createNewGame() {
        soundManager.stopAllSounds();
        soundManager.stopBackgroundMusic(); // Ensure stopped
        soundManager.playBackgroundMusic(); // Restart BGM
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Stops the current game and cleans up resources like sound.
     */
    @Override
    public void stopGame() {
        soundManager.stopAllSounds();
        soundManager.stopBackgroundMusic();
        soundManager.cleanup();
    }
}
