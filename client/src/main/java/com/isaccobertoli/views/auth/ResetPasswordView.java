package com.isaccobertoli.views.auth;

import com.isaccobertoli.components.AuthFormWrapper;

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

public class ResetPasswordView {
    private StackPane rootContainer = new StackPane();
    private TextField emailField;
    private Hyperlink loginLink;
    private Button resetButton;
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

        VBox body = new VBox(new VBox(new Label("Email"), emailField, emailErrorLabel));

        resetButton = new Button("RESET PASSWORD");
        resetButton.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.LARGE);
        resetButton.setDefaultButton(true);
        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setDisable(true);

        loginLink = new Hyperlink("Sign in");
        Label loginLinkLabel = new Label("Already have an account?");
        loginLinkLabel.getStyleClass().addAll(Styles.TEXT_MUTED);

        HBox footerLinks = new HBox(10, loginLinkLabel, loginLink);
        footerLinks.setAlignment(Pos.CENTER);

        VBox footer = new VBox(20, resetButton, footerLinks);
        footer.setAlignment(Pos.CENTER);

        AuthFormWrapper resetPasswordForm = new AuthFormWrapper(
                "Reset your password",
                body,
                footer);

        VBox resetFormContainer = new VBox();
        resetFormContainer.setAlignment(Pos.CENTER);
        resetFormContainer.getChildren().add(resetPasswordForm);

        rootContainer.getChildren().add(resetFormContainer);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    private void updateFormValidity() {
        boolean isValid = validateEmail();

        resetButton.setDisable(!isValid);
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

    public TextField getEmailField() {
        return emailField;
    }

    public Hyperlink getLoginLink() {
        return loginLink;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
