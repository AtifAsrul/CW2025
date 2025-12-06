package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width = Constants.BOARD_WIDTH;
    private final int height = Constants.BOARD_HEIGHT;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] boardMatrix;
    private Point brickOffset;
    private final Score score;

    /**
     * Creates a new game board with the given width and height.
     * Initializes the background matrix, brick generator, rotator, and score
     * system.
     */
    public SimpleBoard() {
        boardMatrix = new int[height][width];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Creates a new falling brick at the starting position.
     * 
     * @return true if the brick immediately collides (meaning game over)
     */
    private boolean spawnBrick() {
        Brick newBrick = brickGenerator.getBrick();
        brickRotator.setBrick(newBrick);

        // Use your new constant names
        int brickWidth = newBrick.getShapeMatrix().get(0).length;
        int centeredX = (width - brickWidth) / 2;
        brickOffset = new Point(centeredX, Constants.START_Y);

        return MatrixOperations.intersect(
                boardMatrix,
                brickRotator.getCurrentShape(),
                (int) brickOffset.getX(),
                (int) brickOffset.getY());
    }

    /**
     * Attempts to move the active brick by a given horizontal/vertical offset.
     * Performs collision detection before committing the move.
     *
     * @param dx the change in X position (e.g., -1 left, +1 right)
     * @param dy the change in Y position (e.g., +1 down)
     * @return true if the move is valid and applied; false if blocked
     */
    private boolean tryMove(int dx, int dy) {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        Point p = new Point(brickOffset);
        p.translate(dx, dy);

        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                p.x,
                p.y);
        if (conflict)
            return false;

        brickOffset = p;
        return true;
    }

    /**
     * Attempts to move the falling brick one cell downward.
     *
     * @return true if movement succeeds; false if blocked by collision
     */
    @Override
    public boolean moveBrickDown() {
        return tryMove(0, 1);
    }

    /**
     * Attempts to move the falling brick one cell to the left.
     *
     * @return true if movement succeeds; false if collision prevents movement
     */
    @Override
    public boolean moveBrickLeft() {
        return tryMove(-1, 0);
    }

    /**
     * Attempts to move the falling brick one cell to the left.
     *
     * @return true if movement succeeds; false if collision prevents movement
     */
    @Override
    public boolean moveBrickRight() {
        return tryMove(1, 0);
    }

    /**
     * Attempts to move the falling brick one cell to the right.
     *
     * @return true if movement succeeds; false if collision prevents movement
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) brickOffset.getX(),
                (int) brickOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Public-facing method used by the game controller to spawn a new brick.
     * Delegates creation to {@link #spawnBrick()}.
     *
     * @return true if spawning the brick results in an immediate collision
     */
    @Override
    public boolean createNewBrick() {
        return spawnBrick();
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    private int calculateGhostY() {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        int ghostY = (int) brickOffset.getY();
        while (true) {
            boolean conflict = MatrixOperations.intersect(
                    currentMatrix,
                    brickRotator.getCurrentShape(),
                    (int) brickOffset.getX(),
                    ghostY + 1);
            if (conflict) {
                break;
            }
            ghostY++;
        }
        return ghostY;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) brickOffset.getX(), (int) brickOffset.getY(),
                calculateGhostY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    /**
     * Locks the current falling brick permanently into the board's background
     * matrix. After this operation, the brick becomes part of the static field.
     */
    @Override
    public void lockBrickToBoard() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) brickOffset.getX(),
                (int) brickOffset.getY());
    }

    /**
     * Checks the board for any fully filled rows and removes them.
     * The board above any cleared rows is shifted downward.
     *
     * @return a ClearRow object containing the number of removed rows,
     *         the updated matrix, and the score bonus earned.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game state by clearing the board, resetting the score,
     * and spawning a new brick at the top of the board.
     */

    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
