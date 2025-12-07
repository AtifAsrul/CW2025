package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main application class that extends {@link Application}.
 * Responsible for initializing and starting the JavaFX application window.
 */
public class Main extends Application {

    private static final String APP_TITLE = "TetrisJFX";
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;

    /**
     * Entry point for the JavaFX application.
     * Loads the MainMenu FXML file and sets up the primary stage.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws Exception if there is an error loading the FXML file or resources.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("MainMenu.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle(APP_TITLE);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main method to launch the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
