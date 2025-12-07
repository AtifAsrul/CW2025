package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private final Label scoreValue;

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

    public void setScore(int score) {
        scoreValue.setText(String.valueOf(score));
    }
}
