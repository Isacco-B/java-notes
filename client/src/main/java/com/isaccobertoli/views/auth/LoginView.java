package com.isaccobertoli.views.auth;

import com.isaccobertoli.components.AuthFormWrapper;
import com.isaccobertoli.components.TogglePasswordField;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView {
    private StackPane rootContainer = new StackPane();
    private TextField emailField;
    private TogglePasswordField passwordField;
    private Hyperlink forgotPasswordLink;
    private Hyperlink registerLink;
    private Button loginButton;
    private Label emailErrorLabel;

    public Scene createScene() {
        emailField = new TextField();
        emailField.setPromptText("Email");

        emailErrorLabel = new Label();
        emailErrorLabel.getStyleClass().addAll(Styles.DANGER);
        emailErrorLabel.setVisible(false);

        emailField.textProperty().addListener((obs, oldText, newText) -> {
            validateEmail();
            updateFormValidity();
        });

        passwordField = new TogglePasswordField();
        passwordField.setPromptText("Password");

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            validatePassword();
            updateFormValidity();
        });

        forgotPasswordLink = new Hyperlink("Forgot password?");
        forgotPasswordLink.setPadding(new Insets(10, 0, 0, 0));

        VBox body = new VBox(
                new VBox(new Label("Email"), emailField, emailErrorLabel),
                new VBox(new Label("Password"), passwordField),
                forgotPasswordLink);

        loginButton = new Button("SIGN IN");
        loginButton.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.LARGE);
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDisable(true);

        registerLink = new Hyperlink("Sign up");
        Label registerLinkLabel = new Label("Don't have an account?");
        registerLinkLabel.getStyleClass().addAll(Styles.TEXT_MUTED);
        HBox footerLinks = new HBox(10, registerLinkLabel, registerLink);
        footerLinks.setAlignment(Pos.CENTER);

        VBox footer = new VBox(20, loginButton, footerLinks);
        footer.setPadding(new Insets(10, 0, 0, 0));
        footer.setAlignment(Pos.CENTER);

        AuthFormWrapper loginForm = new AuthFormWrapper(
                "Login to your account",
                body,
                footer);

        VBox loginFormContainer = new VBox();
        loginFormContainer.setAlignment(Pos.CENTER);
        loginFormContainer.getChildren().add(loginForm);

        rootContainer.getChildren().add(loginFormContainer);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    private void updateFormValidity() {
        boolean isValid = validateEmail() && validatePassword();
        loginButton.setDisable(!isValid);
    }

    private boolean validateEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            return false;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailErrorLabel.setText("Invalid email format.");
            emailErrorLabel.setVisible(true);
            emailField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return false;
        }
        emailErrorLabel.setVisible(false);
        emailField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        return true;
    }

    private boolean validatePassword() {
        String password = passwordField.getPassword();
        return !password.isEmpty();
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordTextField getPasswordField() {
        return passwordField;
    }

    public Hyperlink getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public Hyperlink getRegisterLink() {
        return registerLink;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
