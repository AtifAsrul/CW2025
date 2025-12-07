package com.comp2042;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Main Menu screen.
 * Handles the interaction for starting a new game or exiting the application.
 */
public class MainMenuController {

    /**
     * Initializes the controller.
     * Loads necessary fonts and logs initialization.
     */
    @FXML
    public void initialize() {
        System.out.println("Main Menu Loaded");
        try {
            javafx.scene.text.Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(),
                    38);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for the "Start Game" button.
     * Loads the game layout and switches the scene to the game view.
     *
     * @param event the action event triggering this handler.
     */
    @FXML
    public void onStartGame(ActionEvent event) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            ResourceBundle resources = null;
            FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 700);
            stage.setScene(scene);
            stage.show();

            new GameController(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for the "Exit" button.
     * Terminates the application.
     *
     * @param event the action event triggering this handler.
     */
    @FXML
    public void onExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
