package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Controller class for the main Game GUI.
 * Manages the UI elements, updates the game view, and handles user input via
 * event listeners.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private BorderPane gameBoard;

    @FXML
    private StackPane notificationPanel;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private GridPane heldBrickPanel;

    @FXML
    private Label pausedLabel;

    @FXML
    private Label levelValue;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private GridPane ghostPanel;

    private Rectangle[][] ghostRectangles;

    private Rectangle[][] nextRectangles;

    private Rectangle[][] heldRectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private int currentLevel = 1;

    /**
     * Initializes the controller class.
     * Sets up fonts, panels, visuals, and key event listeners.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        setupGamePanel();
        setupGameOverPanel();
        setupVisuals();
    }

    private void setupGamePanel() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        moveDown(new MoveEvent(EventType.DROP, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                        refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N || keyEvent.getCode() == KeyCode.R) {
                    newGame(null);
                }
                if (keyEvent.getCode() == KeyCode.P) {
                    pauseGame(null);
                }
            }
        });
    }

    private void setupGameOverPanel() {
        gameOverPanel.setVisible(false);
    }

    private void setupVisuals() {
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Initializes the game board view with the given matrix and current brick.
     *
     * @param boardMatrix the current state of the board.
     * @param brick       the current moving brick view data.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorHelper.getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(
                gameBoard.getLayoutX() + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                        + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(
                -42 + gameBoard.getLayoutY() + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap()
                        + brick.getyPosition() * BRICK_SIZE);

        ghostPanel = new GridPane();
        ghostPanel.setVgap(brickPanel.getVgap());
        ghostPanel.setHgap(brickPanel.getHgap());
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorHelper.getFillColor(brick.getBrickData()[i][j]));
                rectangle.setOpacity(0.3); // Ghost effect
                ghostRectangles[i][j] = rectangle;
                ghostPanel.add(rectangle, j, i);
            }
        }
        javafx.scene.layout.Pane parent = (javafx.scene.layout.Pane) brickPanel.getParent();
        int brickIndex = parent.getChildren().indexOf(brickPanel);
        parent.getChildren().add(brickIndex, ghostPanel);

        ghostPanel.setLayoutX(
                gameBoard.getLayoutX() + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                        + brick.getxPosition() * BRICK_SIZE);
        ghostPanel.setLayoutY(
                -42 + gameBoard.getLayoutY() + gamePanel.getLayoutY() + brick.getGhostY() * brickPanel.getHgap()
                        + brick.getGhostY() * BRICK_SIZE);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        nextRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                nextRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
        refreshNextBrick(brick.getNextBrickData());

        heldRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                heldRectangles[i][j] = rectangle;
                heldBrickPanel.add(rectangle, j, i);
            }
        }
        refreshHeldBrick(brick.getHeldBrickData());
    }

    /**
     * Refreshes the display of the held brick.
     *
     * @param heldBrickData the matrix data of the held brick.
     */
    private void refreshHeldBrick(int[][] heldBrickData) {
        if (heldBrickData == null) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    heldRectangles[i][j].setFill(Color.TRANSPARENT);
                    heldRectangles[i][j].setStroke(Color.TRANSPARENT);
                }
            }
            return;
        }
        // Clear previous
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                heldRectangles[i][j].setFill(Color.TRANSPARENT);
                heldRectangles[i][j].setStroke(Color.TRANSPARENT);
            }
        }
        // Draw new
        for (int i = 0; i < heldBrickData.length; i++) {
            for (int j = 0; j < heldBrickData[i].length; j++) {
                if (heldBrickData[i][j] != 0) {
                    setRectangleData(heldBrickData[i][j], heldRectangles[i][j]);
                }
            }
        }
    }

    /**
     * Refreshes the display of the next upcoming brick.
     *
     * @param nextBrickData the matrix data of the next brick.
     */
    private void refreshNextBrick(int[][] nextBrickData) {
        // Clear previous
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nextRectangles[i][j].setFill(Color.TRANSPARENT);
                nextRectangles[i][j].setStroke(Color.TRANSPARENT);
            }
        }
        // Draw new
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                if (nextBrickData[i][j] != 0) {
                    setRectangleData(nextBrickData[i][j], nextRectangles[i][j]);
                }
            }
        }
    }

    /**
     * Refreshes the view of the current falling brick and its ghost.
     *
     * @param brick the view data of the current brick.
     */
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(
                    gameBoard.getLayoutX() + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                            + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(
                    -42 + gameBoard.getLayoutY() + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap()
                            + brick.getyPosition() * BRICK_SIZE);

            ghostPanel.setLayoutX(
                    gameBoard.getLayoutX() + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                            + brick.getxPosition() * BRICK_SIZE);
            ghostPanel.setLayoutY(
                    -42 + gameBoard.getLayoutY() + gamePanel.getLayoutY() + brick.getGhostY() * brickPanel.getHgap()
                            + brick.getGhostY() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    setRectangleData(brick.getBrickData()[i][j], ghostRectangles[i][j]);
                    ghostRectangles[i][j].setOpacity(0.3);
                }
            }
            refreshNextBrick(brick.getNextBrickData());
            refreshHeldBrick(brick.getHeldBrickData());
        }
    }

    /**
     * Refreshes the entire game board background grid.
     *
     * @param board the board matrix data.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Helper method to set the properties of a rectangle in the grid.
     *
     * @param color     the color index for the rectangle.
     * @param rectangle the rectangle object to update.
     */
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(ColorHelper.getFillColor(color));
        rectangle.setArcHeight(5); // Reduced from 9 for better "brick" look
        rectangle.setArcWidth(5);
        if (color == 0) {
            // Empty grid cell: Super dim
            rectangle.setStroke(Color.rgb(255, 255, 255, 0.05));
            rectangle.setStrokeWidth(1);
        } else {
            // Active brick: High contrast
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(2);
        }
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE); // Ensure border doesn't overflow
    }

    /**
     * Moves the brick down and handles clearing rows and particle effects if the
     * move results in a lock.
     *
     * @param event the move event.
     */
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData;
            if (event.getEventType() == EventType.DROP) {
                downData = eventListener.onDropEvent(event);
            } else {
                downData = eventListener.onDownEvent(event);
            }
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notification = new NotificationPanel(
                        "+" + downData.getClearRow().getScoreBonus());
                notificationPanel.getChildren().add(notification);
                notification.showScore(notificationPanel.getChildren());

                // Spawn particles for cleared rows
                spawnParticles(downData.getClearRow().getClearedRows());

                // Show TETRIS text if 4 lines cleared
                if (downData.getClearRow().getLinesRemoved() >= 4) {
                    // Calculate Y position (average of cleared rows)
                    java.util.List<Integer> clearedRows = downData.getClearRow().getClearedRows();
                    double startY = gameBoard.getLayoutY() + gamePanel.getLayoutY();
                    double totalRowIndex = 0;
                    for (Integer r : clearedRows)
                        totalRowIndex += r;
                    double avgRow = totalRowIndex / clearedRows.size();

                    double yPos = startY + (avgRow - 2) * BRICK_SIZE;

                    showTetrisText(yPos);
                }
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Displays a "TETRIS" text animation for a Tetris line clear (4 lines).
     *
     * @param yPos the vertical position to display the text.
     */
    private void showTetrisText(double yPos) {
        javafx.scene.text.Text tetrisText = new javafx.scene.text.Text("TETRIS");
        tetrisText.getStyleClass().add("tetrisTextStyle");

        // Add to ROOT pane for positioning over the board
        javafx.scene.layout.Pane root = (javafx.scene.layout.Pane) gamePanel.getScene().getRoot();
        root.getChildren().add(tetrisText);

        double startX = gameBoard.getLayoutX() + gamePanel.getLayoutX();
        double centerX = startX + (10 * BRICK_SIZE) / 2.0 - 50;

        tetrisText.setLayoutX(centerX);
        tetrisText.setLayoutY(yPos);

        // Animation
        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(1200),
                tetrisText);
        tt.setByY(-60); // Float up

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(1200), tetrisText);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(Duration.millis(250), tetrisText);
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(1.1); // Slightly larger than normal
        st.setToY(1.1);
        st.setCycleCount(2);
        st.setAutoReverse(true);

        javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, ft, st);
        pt.setOnFinished(e -> root.getChildren().remove(tetrisText));
        pt.play();
    }

    /**
     * Spawns particle effects for cleared rows.
     *
     * @param rows list of row indices that were cleared.
     */
    private void spawnParticles(java.util.List<Integer> rows) {
        // Root pane to add particles to
        javafx.scene.layout.Pane root = (javafx.scene.layout.Pane) gamePanel.getScene().getRoot();

        double startX = gameBoard.getLayoutX() + gamePanel.getLayoutX();
        double startY = gameBoard.getLayoutY() + gamePanel.getLayoutY();

        for (Integer row : rows) {
            // Visual row index is row - 2
            double y = startY + (row - 2) * BRICK_SIZE;

            for (int i = 0; i < 25; i++) { // Generate 25 particles per row
                javafx.scene.shape.Circle particle = new javafx.scene.shape.Circle(Math.random() * 3 + 2);
                // Random neon colors
                int colorIdx = (int) (Math.random() * 7) + 1;
                particle.setFill(ColorHelper.getFillColor(colorIdx));

                // Add glow to particle
                javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
                glow.setColor((javafx.scene.paint.Color) ColorHelper.getFillColor(colorIdx));
                glow.setRadius(5);
                glow.setSpread(0.5);
                particle.setEffect(glow);

                double x = startX + Math.random() * (10 * BRICK_SIZE);
                particle.setLayoutX(x);
                particle.setLayoutY(y + Math.random() * BRICK_SIZE);

                root.getChildren().add(particle);

                // Animate
                javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(800),
                        particle);
                tt.setByX((Math.random() - 0.5) * 150);
                tt.setByY((Math.random() - 0.5) * 150 - 20); // Float up slightly

                javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(800),
                        particle);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);

                javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, ft);
                pt.setOnFinished(e -> root.getChildren().remove(particle));
                pt.play();
            }
        }
    }

    /**
     * Sets the input event listener for this controller.
     *
     * @param eventListener the event listener to handle game input events.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @FXML
    private Label scoreValue;

    /**
     * Binds the score property to the score label.
     *
     * @param integerProperty the score property from the game logic.
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreValue.textProperty().bind(integerProperty.asString("%d"));
        integerProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateLevel(newValue.intValue());
            }
        });
    }

    /**
     * Updates the current level based on the score.
     *
     * @param score the current score.
     */
    private void updateLevel(int score) {
        int newLevel = LevelConfig.getLevelForScore(score);
        if (newLevel != currentLevel) {
            currentLevel = newLevel;
            double newSpeed = LevelConfig.getSpeedForLevel(currentLevel);

            System.out.println("Level Up! New Level: " + currentLevel + ", Speed: " + newSpeed + "ms");

            if (timeLine != null) {
                timeLine.stop();
                timeLine.getKeyFrames().clear();
                timeLine.getKeyFrames().add(new KeyFrame(
                        Duration.millis(newSpeed),
                        ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
                timeLine.play();
            }

            // Update Level Label
            if (levelValue != null) {
                if (currentLevel >= 5) {
                    levelValue.setText("∞");
                } else {
                    levelValue.setText(String.valueOf(currentLevel));
                }
            }
        }
    }

    @FXML
    private javafx.scene.layout.VBox holdBox;
    @FXML
    private javafx.scene.layout.VBox scoreBox;
    @FXML
    private javafx.scene.layout.VBox nextBox;

    private Rectangle dimOverlay;

    /**
     * Toggles a semi-transparent dimmer overlay on the game.
     *
     * @param show true to show the dimmer, false to remove it.
     */
    private void toggleDimmer(boolean show) {
        if (show) {
            if (dimOverlay == null) {
                dimOverlay = new Rectangle(0, 0, 600, 700);
                dimOverlay.setFill(Color.rgb(0, 0, 0, 0.85));
                if (notificationPanel.getChildren().isEmpty()) {
                    notificationPanel.getChildren().add(dimOverlay);
                } else {
                    notificationPanel.getChildren().add(0, dimOverlay);
                }
            }
        } else {
            if (dimOverlay != null) {
                notificationPanel.getChildren().remove(dimOverlay);
                dimOverlay = null;
            }
        }
    }

    /**
     * Displays the Game Over screen and handles the associated animations.
     */
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);

        // Set Score in Panel
        try {
            int score = Integer.parseInt(scoreValue.getText());
            gameOverPanel.setScore(score);
        } catch (NumberFormatException e) {
            gameOverPanel.setScore(0);
        }

        // Fade Animations
        FadeTransition ftScore = new FadeTransition(Duration.millis(500), scoreBox);
        ftScore.setToValue(0.0);
        ftScore.play();

        FadeTransition ftHold = new FadeTransition(Duration.millis(500), holdBox);
        ftHold.setToValue(0.3); // Dim
        ftHold.play();

        FadeTransition ftNext = new FadeTransition(Duration.millis(500), nextBox);
        ftNext.setToValue(0.3); // Dim
        ftNext.play();

        // Red Flash Effect on Root
        javafx.scene.layout.Pane root = (javafx.scene.layout.Pane) gamePanel.getScene().getRoot();
        Rectangle flash = new Rectangle(root.getWidth(), root.getHeight(), Color.RED);
        flash.setOpacity(0.0);
        root.getChildren().add(flash);

        FadeTransition ftFlash = new FadeTransition(Duration.millis(150), flash);
        ftFlash.setFromValue(0.0);
        ftFlash.setToValue(0.3);
        ftFlash.setCycleCount(2);
        ftFlash.setAutoReverse(true);
        ftFlash.setOnFinished(e -> root.getChildren().remove(flash));
        ftFlash.play();

        toggleDimmer(true);
    }

    /**
     * Starts a new game, resetting UI elements and game state.
     *
     * @param actionEvent the event triggering the new game (can be null).
     */
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        toggleDimmer(false);

        // Reset Opacity
        scoreBox.setOpacity(1.0);
        holdBox.setOpacity(1.0);
        nextBox.setOpacity(1.0);

        // Reset Level first to prevent updateLevel listener from triggering logic
        currentLevel = 1;
        if (levelValue != null) {
            levelValue.setText("1");
        }

        eventListener.createNewGame();

        double speed = LevelConfig.getSpeedForLevel(currentLevel);
        timeLine.getKeyFrames().clear();
        timeLine.getKeyFrames().add(new KeyFrame(
                Duration.millis(speed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));

        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    /**
     * Toggles the pause state of the game.
     *
     * @param actionEvent the event triggering the pause (can be null).
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (isGameOver.getValue() == Boolean.FALSE) {
            if (isPause.getValue() == Boolean.FALSE) {
                isPause.setValue(Boolean.TRUE);
                timeLine.stop();
                pausedLabel.setVisible(true);
                toggleDimmer(true);
            } else {
                isPause.setValue(Boolean.FALSE);
                timeLine.play();
                pausedLabel.setVisible(false);
                toggleDimmer(false);
            }
        }
        gamePanel.requestFocus();
    }

    /**
     * Returns to the main menu.
     *
     * @param actionEvent the event triggering the return.
     */
    public void backToMenu(ActionEvent actionEvent) {
        timeLine.stop();
        if (eventListener != null) {
            eventListener.stopGame();
        }
        try {
            URL location = getClass().getClassLoader().getResource("MainMenu.fxml");
            ResourceBundle resources = null;
            FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 700);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
