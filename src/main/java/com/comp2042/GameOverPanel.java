package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Custom JavaFX component representing the Game Over screen.
 * Displays the final score and a prompt to restart.
 */
public class GameOverPanel extends VBox {

    private final Label scoreValue;

    /**
     * Constructs the Game Over panel.
     * Sets up the layout, style, and labels.
     */
    public GameOverPanel() {
        getStyleClass().add("gameOverPanelContainer");

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        final Label scoreTitle = new Label("Score");
        scoreTitle.getStyleClass().add("gameOverScoreLabel");

        scoreValue = new Label("0");
        scoreValue.getStyleClass().add("gameOverScoreValue");

        final Label promptLabel = new Label("Press R to Restart");
        promptLabel.getStyleClass().add("gameOverPrompt");

        getChildren().addAll(gameOverLabel, scoreTitle, scoreValue, promptLabel);
    }

    /**
     * Sets the score to display on the panel.
     *
     * @param score the final game score.
     */
    public void setScore(int score) {
        scoreValue.setText(String.valueOf(score));
    }
}
