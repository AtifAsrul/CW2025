package com.comp2042;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard();
    private final GuiController viewGuiController;
    private final SoundManager soundManager;

    public GameController(GuiController c) {
        viewGuiController = c;
        soundManager = new SoundManager();
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        soundManager.playBackgroundMusic(); // Start BGM
    }

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

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        if (board.holdBrick()) {
            soundManager.playHold();
        }
        return board.getViewData();
    }

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

    @Override
    public void createNewGame() {
        soundManager.stopAllSounds();
        soundManager.stopBackgroundMusic(); // Ensure stopped
        soundManager.playBackgroundMusic(); // Restart BGM
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public void stopGame() {
        soundManager.stopAllSounds();
        soundManager.stopBackgroundMusic();
        soundManager.cleanup();
    }
}
