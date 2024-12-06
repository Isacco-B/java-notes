package com.isaccobertoli.views.note;

import com.isaccobertoli.components.NoteFormWrapper;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CreateNoteView {
    private StackPane rootContainer = new StackPane();
    private TextField titleField;
    private TextArea contentField;
    private Label titleErrorLabel;
    private Label contentErrorLabel;
    private Button createButton;
    private Button cancelButton;

    public Scene createScene() {
        titleField = new TextField();
        titleField.setPromptText("Title");

        titleErrorLabel = new Label();
        titleErrorLabel.getStyleClass().addAll(Styles.DANGER);
        titleErrorLabel.setVisible(false);

        titleField.textProperty().addListener((obs, oldText, newText) -> {
            validateTitle();
            updateFormValidity();
        });

        contentField = new TextArea();
        contentField.setPromptText("Content");
        contentField.setStyle("-fx-padding: 10 0;");
        contentField.setWrapText(true);

        contentErrorLabel = new Label();
        contentErrorLabel.getStyleClass().addAll(Styles.DANGER);
        contentErrorLabel.setVisible(false);

        contentField.textProperty().addListener((obs, oldText, newText) -> {
            validateContent();
            updateFormValidity();
        });

        VBox body = new VBox(
                new VBox(new Label("Title"), titleField, titleErrorLabel),
                new VBox(new Label("Content"), contentField, contentErrorLabel));

        createButton = new Button("CREATE");
        createButton.getStyleClass().add(Styles.TEXT_BOLD);
        createButton.setDefaultButton(true);
        createButton.setMaxWidth(300);
        createButton.setMinWidth(300);
        createButton.setDisable(true);

        cancelButton = new Button("CANCEL");
        cancelButton.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.BUTTON_OUTLINED);
        cancelButton.setMaxWidth(300);
        cancelButton.setMinWidth(300);

        HBox buttonContainer = new HBox(20, createButton, cancelButton);

        NoteFormWrapper createNoteForm = new NoteFormWrapper(
                "Create Note",
                body,
                buttonContainer);

        VBox createNoteContainer = new VBox();
        createNoteContainer.setAlignment(Pos.CENTER);
        createNoteContainer.setPadding(new Insets(40));
        createNoteContainer.setMaxSize(1200, 800);
        createNoteContainer.getChildren().addAll(createNoteForm);

        rootContainer.getChildren().add(createNoteContainer);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    private void updateFormValidity() {
        boolean isValid = validateTitle() && validateContent();
        createButton.setDisable(!isValid);
    }

    private boolean validateTitle() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            titleErrorLabel.setText("Title is required.");
            titleErrorLabel.setVisible(true);
            titleErrorLabel.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return false;
        }
        titleErrorLabel.setVisible(false);
        titleErrorLabel.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        return true;
    }

    private boolean validateContent() {
        String content = contentField.getText().trim();
        if (content.isEmpty()) {
            contentErrorLabel.setText("Content is required.");
            contentErrorLabel.setVisible(true);
            contentErrorLabel.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            return false;
        }
        contentErrorLabel.setVisible(false);
        contentErrorLabel.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        return true;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public TextArea getContentField() {
        return contentField;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
