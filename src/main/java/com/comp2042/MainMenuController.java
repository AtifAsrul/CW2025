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

public class MainMenuController {

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

    @FXML
    public void onExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
