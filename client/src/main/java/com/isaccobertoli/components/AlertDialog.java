package com.isaccobertoli.components;

import java.util.function.Consumer;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AlertDialog {
    private final Dialog dialog;
    private final Button confirmButton;
    private final Button cancelButton;

    private static class Dialog extends VBox {
        public Dialog(int width, int height) {
            super();
            setSpacing(20);
            setPadding(new Insets(20));
            setMaxSize(width, height);
            setStyle(
                    "-fx-background-color: -color-bg-default; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        }
    }

    public AlertDialog(String title, String content) {
        dialog = new Dialog(600, 200);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_BOLD);

        Label contentLabel = new Label(content);
        contentLabel.setMinHeight(40);
        contentLabel.setMaxHeight(40);
        contentLabel.setWrapText(true);
        contentLabel.setAlignment(Pos.TOP_LEFT);
        contentLabel.getStyleClass().addAll(Styles.TEXT_MUTED);

        confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add(Styles.TEXT_BOLD);
        confirmButton.setDefaultButton(true);

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.TEXT_BOLD);

        HBox actionBtnContainer = new HBox(10, cancelButton, confirmButton);
        actionBtnContainer.setAlignment(Pos.BOTTOM_RIGHT);

        dialog.getChildren().addAll(titleLabel, contentLabel, actionBtnContainer);
    }

    public VBox getDialogContent() {
        return dialog;
    }

    public void setOnConfirm(Consumer<Void> action) {
        confirmButton.setOnAction(event -> action.accept(null));
    }

    public void setOnCancel(Consumer<Void> action) {
        cancelButton.setOnAction(event -> action.accept(null));
    }


}
