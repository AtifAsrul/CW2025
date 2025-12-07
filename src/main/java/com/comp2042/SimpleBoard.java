package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 * Concrete implementation of the {@link Board} interface.
 * Manages the game state, including the grid, active brick, and score.
 */
public class SimpleBoard implements Board {

    private final int width = Constants.BOARD_WIDTH;
    private final int height = Constants.BOARD_HEIGHT;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] boardMatrix;
    private Point brickOffset;
    private final Score score;
    private Brick heldBrick;
    private boolean hasHeldThisTurn;

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

        brickRotator.setBrick(newBrick);
        hasHeldThisTurn = false;

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
     * Attempts to move the falling brick one cell to the right.
     *
     * @return true if movement succeeds; false if collision prevents movement
     */
    @Override
    public boolean moveBrickRight() {
        return tryMove(1, 0);
    }

    /**
     * Attempts to rotate the falling brick to the left.
     *
     * @return true if rotation succeeds; false if collision prevents rotation
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

    /**
     * Holds the current brick. If a brick is already held, swaps it with the
     * current one.
     *
     * @return true if the brick was successfully held/swapped, false if a hold
     *         action occurred this turn.
     */
    @Override
    public boolean holdBrick() {
        if (hasHeldThisTurn) {
            return false;
        }

        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            heldBrick = currentBrick;
            // Spawn a new brick since we didn't have one held
            spawnBrick();
            // Important: holding counts as a turn action, so set flag AFTER spawn which
            // resets it
            hasHeldThisTurn = true;
            return true;
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);

            // Reset position for swapped brick
            int brickWidth = temp.getShapeMatrix().get(0).length;
            int centeredX = (width - brickWidth) / 2;
            brickOffset = new Point(centeredX, Constants.START_Y);

            hasHeldThisTurn = true;

            // Check collision for the swapped brick immediately?
            // Usually swapping doesn't kill you unless you spawn inside?
            // But for now let's just assume valid swap.
            return true;
        }
    }

    /**
     * Gets the current board matrix.
     *
     * @return the 2D integer array representing the board.
     */
    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Calculates the Y position of the ghost brick (drop position).
     *
     * @return the Y coordinate for the ghost brick.
     */
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

    /**
     * Generates the view data for the current game state, including brick position,
     * ghost position, and next/held bricks.
     *
     * @return the ViewData object.
     */
    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) brickOffset.getX(), (int) brickOffset.getY(),
                calculateGhostY(), brickGenerator.getNextBrick().getShapeMatrix().get(0),
                heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null);
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

    /**
     * Gets the score object tracking the game score.
     *
     * @return the Score object.
     */
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
        boardMatrix = new int[height][width];
        score.reset();
        heldBrick = null;
        hasHeldThisTurn = false;
        createNewBrick();
    }
}
