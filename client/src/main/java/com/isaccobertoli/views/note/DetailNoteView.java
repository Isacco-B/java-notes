package com.isaccobertoli.views.note;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;

import com.isaccobertoli.dto.note.NoteDTO;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DetailNoteView {
    private static final int MAX_WIDTH = 1300;
    private StackPane rootContainer = new StackPane();
    private Button goBackBtn = new Button("Go Back", new FontIcon(MaterialDesignA.ARROW_LEFT));
    private Label titleLabel = new Label();
    private Label contentLabel = new Label();
    private Label createdAtLabel = new Label();
    private Label updatedAtLabel = new Label();

    public Scene createScene() {
        BorderPane mainContainer = new BorderPane();
        mainContainer.setMaxWidth(MAX_WIDTH);
        mainContainer.setPadding(new Insets(20));

        VBox headerContainer = new VBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setPadding(new Insets(10));
        goBackBtn.setAlignment(Pos.CENTER_LEFT);
        goBackBtn.setDefaultButton(true);
        headerContainer.getChildren().add(goBackBtn);

        VBox detailsContainer = new VBox(10);
        detailsContainer.setAlignment(Pos.TOP_LEFT);
        detailsContainer.setPadding(new Insets(20));
        detailsContainer
                .setStyle("-fx-background-color: -color-light; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        titleLabel.getStyleClass().add(Styles.TITLE_1);
        contentLabel.getStyleClass().add(Styles.TEXT);
        createdAtLabel.getStyleClass().add(Styles.TEXT_MUTED);
        updatedAtLabel.getStyleClass().add(Styles.TEXT_MUTED);

        contentLabel.setWrapText(true);

        detailsContainer.getChildren().addAll(
                new Label("Title:"),
                titleLabel,
                new Label("Content:"),
                contentLabel,
                new Separator(Orientation.HORIZONTAL),
                new Label("Created At:"),
                createdAtLabel,
                new Label("Updated At:"),
                updatedAtLabel);

        ScrollPane scrollPane = new ScrollPane(detailsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");
        scrollPane.setPadding(new Insets(10));

        mainContainer.setTop(headerContainer);
        mainContainer.setCenter(scrollPane);

        rootContainer.getChildren().add(mainContainer);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    public void updateNoteDetails(NoteDTO note) {
        titleLabel.setText(note.getTitle());
        contentLabel.setText(note.getContent());
        createdAtLabel.setText(note.getCreatedAt());
        updatedAtLabel.setText(note.getUpdatedAt());
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

    public Button getGoBackBtn() {
        return goBackBtn;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
