package com.isaccobertoli.views.note;

import com.isaccobertoli.components.NoteFormWrapper;
import com.isaccobertoli.dto.note.NoteDTO;

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

public class UpdateNoteView {
    private StackPane rootContainer = new StackPane();
    private Button goBackBtn = new Button("Go Back");
    private TextField titleField;
    private TextArea contentField;
    private Label titleErrorLabel;
    private Label contentErrorLabel;
    private Button updateButton;
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

        updateButton = new Button("UPDATE");
        updateButton.getStyleClass().add(Styles.TEXT_BOLD);
        updateButton.setDefaultButton(true);
        updateButton.setMaxWidth(300);
        updateButton.setMinWidth(300);
        updateButton.setDisable(true);

        cancelButton = new Button("CANCEL");
        cancelButton.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.BUTTON_OUTLINED);
        cancelButton.setMaxWidth(300);
        cancelButton.setMinWidth(300);

        HBox buttonContainer = new HBox(10, updateButton, cancelButton);

        NoteFormWrapper createNoteForm = new NoteFormWrapper(
                "Update Note",
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

    public void updateNoteDetails(NoteDTO note) {
        titleField.setText(note.getTitle());
        contentField.setText(note.getContent());
    }

    public void showErrorCard() {
        rootContainer.getChildren().clear();

        VBox errorContainer = new VBox();
        errorContainer.setSpacing(30);
        errorContainer.setAlignment(Pos.CENTER);

        Label errorLabel = new Label("Something went wrong. Please try again.");
        errorLabel.getStyleClass().add(Styles.TEXT_BOLD);
        goBackBtn.setDefaultButton(true);

        errorContainer.getChildren().addAll(errorLabel, goBackBtn);
        rootContainer.getChildren().add(errorContainer);
    }

    private void updateFormValidity() {
        boolean isValid = validateTitle() && validateContent();
        updateButton.setDisable(!isValid);
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

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getGoBackBtn() {
        return goBackBtn;
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
