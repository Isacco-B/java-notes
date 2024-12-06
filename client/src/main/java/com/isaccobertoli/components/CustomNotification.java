package com.isaccobertoli.components;

import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class CustomNotification {
    private Notification notification;
    private Timeline slideInAnimation;
    private Timeline slideOutAnimation;
    private PauseTransition pause;
    private StackPane stackPane;

    public CustomNotification(StackPane stackPane, String message, FontIcon icon, String... styleClasses) {
        this.stackPane = stackPane;
        notification = new Notification(message, icon);
        initializeNotification(styleClasses);
    }

    public CustomNotification(StackPane stackPane, String message, String... styleClasses) {
        this.stackPane = stackPane;
        notification = new Notification(message);
        initializeNotification(styleClasses);
    }

    private void initializeNotification(String... styleClasses) {
        notification.getStyleClass().add(Styles.ELEVATED_1);
        notification.getStyleClass().addAll(styleClasses);
        notification.setOnClose(e -> Animations.flash(notification).playFromStart());
        notification.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_1);
        notification.setPrefHeight(Region.USE_PREF_SIZE);
        notification.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(notification, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(notification, new Insets(0, 10, 10, 0));

        slideInAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(notification.translateXProperty(), 300)),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(notification.translateXProperty(), 0)));

        slideOutAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(notification.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(250),
                        new KeyValue(notification.translateXProperty(), 300)));

        pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> dismissNotification());

        notification.setOnClose(e -> dismissNotification());

        slideInAnimation.setOnFinished(e -> pause.play());
        slideOutAnimation.setOnFinished(e -> stackPane.getChildren().remove(notification));
    }

    public void showNotification() {
        notification.setTranslateX(300);

        if (stackPane.getChildren().contains(notification)) {
            stackPane.getChildren().remove(notification);
        }

        stackPane.getChildren().add(notification);
        slideInAnimation.playFromStart();
    }

    private void dismissNotification() {
        slideInAnimation.stop();
        pause.stop();

        slideOutAnimation.playFromStart();
    }
}
