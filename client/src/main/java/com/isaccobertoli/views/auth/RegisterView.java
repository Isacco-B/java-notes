package com.isaccobertoli.views.auth;

import com.isaccobertoli.components.AuthFormWrapper;
import com.isaccobertoli.components.TogglePasswordField;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RegisterView {
    private StackPane rootContainer = new StackPane();
    private TextField emailField;
    private TogglePasswordField passwordField;
    private TogglePasswordField confirmPasswordField;
    private Hyperlink loginLink;
    private Button registerButton;
    private Label emailErrorLabel;
    private Label passwordErrorLabel;
    private Label confirmPasswordErrorLabel;

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

        passwordErrorLabel = new Label();
        passwordErrorLabel.getStyleClass().addAll(Styles.DANGER);
        passwordErrorLabel.setVisible(false);

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            validatePassword();
            updateFormValidity();
        });

        confirmPasswordField = new TogglePasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        confirmPasswordErrorLabel = new Label();
        confirmPasswordErrorLabel.getStyleClass().addAll(Styles.DANGER);
        confirmPasswordErrorLabel.setVisible(false);

        confirmPasswordField.textProperty().addListener((obs, oldText, newText) -> {
            validateConfirmPassword();
            updateFormValidity();
        });

        VBox body = new VBox(new VBox(new Label("Email"), emailField, emailErrorLabel),
                new VBox(new Label("Password"), passwordField, passwordErrorLabel),
                new VBox(new Label("Confirm Password"), confirmPasswordField, confirmPasswordErrorLabel));

        registerButton = new Button("SIGN UP");
        registerButton.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.LARGE);
        registerButton.setDefaultButton(true);
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setDisable(true);

        loginLink = new Hyperlink("Sign in");
        Label loginLinkLabel = new Label("Already have an account?");
        loginLinkLabel.getStyleClass().addAll(Styles.TEXT_MUTED);
        HBox footerLinks = new HBox(10, loginLinkLabel, loginLink);
        footerLinks.setAlignment(Pos.CENTER);

        VBox footer = new VBox(20, registerButton, footerLinks);
        footer.setAlignment(Pos.CENTER);

        AuthFormWrapper registerForm = new AuthFormWrapper(
                "Create an account",
                body,
                footer);


        VBox registerFormContainer = new VBox();
        registerFormContainer.setAlignment(Pos.CENTER);
        registerFormContainer.getChildren().add(registerForm);

        rootContainer.getChildren().add(registerFormContainer);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    private void updateFormValidity() {
        boolean isValid = validateEmail() &&
                validatePassword() &&
                validateConfirmPassword();

        registerButton.setDisable(!isValid);
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
        if (password.isEmpty()) {
            return false;
        } else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$")) {
            passwordErrorLabel.setText(
                    "Password must be at least 8 characters long, with one uppercase, one lowercase, one digit and one special character.");
            passwordErrorLabel.setVisible(true);
            passwordErrorLabel.setPrefWidth(400);
            passwordErrorLabel.setMaxWidth(400);
            passwordErrorLabel.setWrapText(true);

            passwordField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return false;
        }
        passwordErrorLabel.setVisible(false);
        passwordErrorLabel.setText("");

        passwordField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        return true;
    }

    private boolean validateConfirmPassword() {
        String password = passwordField.getPassword();
        String confirmPassword = confirmPasswordField.getPassword();
        if (confirmPassword.isEmpty()) {
            return false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordErrorLabel.setText("Passwords do not match.");
            confirmPasswordErrorLabel.setVisible(true);
            confirmPasswordField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return false;
        }
        confirmPasswordErrorLabel.setVisible(false);
        confirmPasswordField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        return true;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordTextField getPasswordField() {
        return passwordField;
    }

    public PasswordTextField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public Hyperlink getLoginLink() {
        return loginLink;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
