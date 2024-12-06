package com.isaccobertoli.controllers.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.views.auth.ResetPasswordView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordController {
    private final ResetPasswordView resetPasswordView;
    private final ApiService apiService = RetrofitClient.getService();

    public ResetPasswordController() {
        this.resetPasswordView = new ResetPasswordView();
    }

    public Scene getScene() {
        Scene resetPasswordScene = resetPasswordView.createScene();
        initialize();
        return resetPasswordScene;
    }

    public void initialize() {
        resetPasswordView.getLoginLink().setOnAction(e -> SceneManager.navigateToLogin());
        resetPasswordView.getResetButton().setOnAction(e -> handleLogin());

        Platform.runLater(() -> {
            Stage stage = (Stage) resetPasswordView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void handleLogin() {
        String email = resetPasswordView.getEmailField().getText();

        setLoadingState(true);

        Call<GenericResponse<String>> call = apiService.passwordReset(email);

        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        setLoadingState(false);
                        resetPasswordView.getEmailField().setText("");
                        resetPasswordView.getResetButton().setDisable(true);
                        showSuccess(response.body().getMessage());
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();

                            showError("Something went wrong!\n" + errorMessage);
                        } catch (Exception e) {
                            showError("Something went wrong!\n" + response.message());
                        } finally {
                            setLoadingState(false);
                        }
                    }

                });
            }

            @Override
            public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                Platform.runLater(() -> {
                    showError("Something went wrong!\n " + t.getMessage());
                    setLoadingState(false);
                });
            }
        });
    }

    private void setLoadingState(boolean loading) {
        resetPasswordView.getResetButton().setDisable(loading);
    }

    private void showError(String message) {
        new CustomNotification(
                resetPasswordView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }

    private void showSuccess(String message) {
        new CustomNotification(
                resetPasswordView.getRootContainer(),
                message).showNotification();
    }
}
