package com.isaccobertoli.components;

import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignE;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NoteCard extends Card {
    private final Button viewBtn;
    private final Button editBtn;
    private final Button deleteBtn;

    public NoteCard(String title, String content, String createdAt, String updatedAt) {
        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add(Styles.TITLE_4);

        Label cardContent = new Label(content);
        cardContent.setMinHeight(50);
        cardContent.setMaxHeight(50);
        cardContent.setAlignment(Pos.TOP_LEFT);
        cardContent.getStyleClass().add(Styles.TEXT_MUTED);
        cardContent.setWrapText(true);

        VBox cardFooter = new VBox();
        cardFooter.setSpacing(10);
        cardFooter.setAlignment(Pos.CENTER);

        HBox createdAtContainer = new HBox();
        Spacer createdAtSpacer = new Spacer();
        HBox.setHgrow(createdAtSpacer, Priority.ALWAYS);
        Label createdAtLabel = new Label("Created: ");
        Label createdAtValue = new Label(createdAt);
        createdAtValue.getStyleClass().add(Styles.TEXT_MUTED);
        createdAtContainer.getChildren().addAll(createdAtLabel, createdAtSpacer, createdAtValue);

        HBox updatedAtContainer = new HBox();
        Spacer updatedAtSpacer = new Spacer();
        HBox.setHgrow(createdAtSpacer, Priority.ALWAYS);
        Label updatedAtLabel = new Label("Updated: ");
        Label updatedAtValue = new Label(updatedAt);
        updatedAtValue.getStyleClass().add(Styles.TEXT_MUTED);
        updatedAtContainer.getChildren().addAll(updatedAtLabel, updatedAtSpacer, updatedAtValue);

        HBox actionBtnContainer = new HBox();
        actionBtnContainer.setSpacing(10);

        viewBtn = new Button(null, new FontIcon(MaterialDesignE.EYE_OUTLINE));
        viewBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);

        editBtn = new Button(null, new FontIcon(MaterialDesignF.FILE_DOCUMENT_EDIT_OUTLINE));
        editBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);

        deleteBtn = new Button(null, new FontIcon(MaterialDesignT.TRASH_CAN_OUTLINE));
        deleteBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE);

        actionBtnContainer.getChildren().addAll(viewBtn, editBtn, deleteBtn);
        cardFooter.getChildren().addAll(createdAtContainer, updatedAtContainer, new Separator(Orientation.HORIZONTAL),
                actionBtnContainer);

        setHeader(cardTitle);
        setBody(cardContent);
        setFooter(cardFooter);

        setPrefWidth(400);
        setMaxWidth(400);
        getStyleClass().addAll("form-card");
    }

    public void setOnView(Consumer<Void> action) {
        viewBtn.setOnAction(event -> action.accept(null));
    }

    public void setOnEdit(Consumer<Void> action) {
        editBtn.setOnAction(event -> action.accept(null));
    }

    public void setOnDelete(Consumer<Void> action) {
        deleteBtn.setOnAction(event -> action.accept(null));
    }
}
