package com.isaccobertoli.components;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.controls.PasswordTextField;
import javafx.scene.Cursor;

public class TogglePasswordField extends PasswordTextField {

    public TogglePasswordField() {
        super();
        initializeToggleFeature();
    }

    private void initializeToggleFeature() {
        var toggleIcon = new FontIcon(Feather.EYE_OFF);
        toggleIcon.setCursor(Cursor.HAND);
        toggleIcon.setOnMouseClicked(e -> {
            boolean revealPassword = !this.getRevealPassword();
            this.setRevealPassword(revealPassword);
            toggleIcon.setIconCode(revealPassword ? Feather.EYE : Feather.EYE_OFF);
        });
        this.setRight(toggleIcon);
    }
}
