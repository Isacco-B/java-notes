package com.isaccobertoli.components;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AuthFormWrapper extends Card {

    public AuthFormWrapper(String subtitle, Node bodyContent, Node footerContent) {
        Text titleText = new Text("Notes");
        titleText.getStyleClass().addAll(Styles.TITLE_1);
        Text subtitleText = new Text(subtitle);
        subtitleText.setStyle("-fx-font-size: 16px;");
        subtitleText.getStyleClass().addAll(Styles.TEXT_MUTED);
        setHeader(new VBox(titleText, subtitleText));

        setBody(bodyContent);

        if (footerContent != null) {
            setFooter(footerContent);
        }

        setPrefWidth(400);
        setMaxWidth(400);

        getStyleClass().addAll("form-card");
    }
}
