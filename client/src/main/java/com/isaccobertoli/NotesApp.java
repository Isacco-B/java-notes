package com.isaccobertoli;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

import com.isaccobertoli.controllers.note.ListNoteController;
import com.isaccobertoli.utils.SceneManager;

import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NotesApp extends Application {
    static final String ASSETS_DIR = "/assets/";
    static final String APP_ICON_PATH = Objects.requireNonNull(
            Launcher.class.getResource(ASSETS_DIR + "icons/app-icon.png")).toExternalForm();
    static final String APP_PROPERTIES_PATH = "/application.properties";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 900;

    public static void startApplication(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

        loadApplicationProperties();

        primaryStage.getIcons().add(new Image(APP_ICON_PATH));
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchScene(new ListNoteController().getScene(), "Game preparation");

    }

    private void loadApplicationProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    Objects.requireNonNull(getClass().getResourceAsStream(APP_PROPERTIES_PATH)),
                    UTF_8));
            properties.forEach((key, value) -> System.setProperty(
                    String.valueOf(key),
                    String.valueOf(value)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
