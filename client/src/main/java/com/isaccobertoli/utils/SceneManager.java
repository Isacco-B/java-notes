package com.isaccobertoli.utils;

import com.isaccobertoli.controllers.auth.LoginController;
import com.isaccobertoli.controllers.auth.RegisterController;
import com.isaccobertoli.controllers.auth.ResetPasswordController;
import com.isaccobertoli.controllers.note.CreateNoteController;
import com.isaccobertoli.controllers.note.DetailNoteController;
import com.isaccobertoli.controllers.note.ListNoteController;
import com.isaccobertoli.controllers.note.UpdateNoteController;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    static final String CSS_PATH = "/assets/css/index.css";
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(Scene scene, String title) {
        scene.getStylesheets().add(primaryStage.getScene() != null
                ? primaryStage.getScene().getStylesheets().get(0)
                : "");

        scene.getStylesheets().add(CSS_PATH);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);

        primaryStage.setWidth(primaryStage.getWidth());
        primaryStage.setHeight(primaryStage.getHeight());

        Platform.runLater(() -> {
            primaryStage.show();
            primaryStage.requestFocus();
        });
    }

    public static void navigateToLogin() {
        SceneManager.switchScene(new LoginController().getScene(), "Login");
    }

    public static void navigateToSignUp() {
        SceneManager.switchScene(new RegisterController().getScene(), "Register");
    }

    public static void navigateToResetPassword() {
        SceneManager.switchScene(new ResetPasswordController().getScene(), "Reset password");
    }

    public static void navigateToListNote() {
        SceneManager.switchScene(new ListNoteController().getScene(), "Notes");
    }

    public static void navigateToCreateNote() {
        SceneManager.switchScene(new CreateNoteController().getScene(), "Create note");
    }

    public static void navigateToEditNote(String noteId) {
        SceneManager.switchScene(new UpdateNoteController(noteId).getScene(), "Update note");
    }

    public static void navigateToDetailNote(String noteId) {
        SceneManager.switchScene(new DetailNoteController(noteId).getScene(), "Detail note");
    }
}
