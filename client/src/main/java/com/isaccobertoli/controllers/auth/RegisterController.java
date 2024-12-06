package com.isaccobertoli.controllers.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.auth.RegisterRequest;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.views.auth.RegisterView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterController {
    private final RegisterView registerView;
    private final ApiService apiService = RetrofitClient.createService();

    public RegisterController() {
        this.registerView = new RegisterView();
    }

    public Scene getScene() {
        Scene registerScene = registerView.createScene();
        initialize();
        return registerScene;
    }

    private void initialize() {
        registerView.getLoginLink().setOnAction(e -> SceneManager.navigateToLogin());
        registerView.getRegisterButton().setOnAction(e -> handleLogin());

        Platform.runLater(() -> {
            Stage stage = (Stage) registerView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void handleLogin() {
        String email = registerView.getEmailField().getText();
        String password = registerView.getPasswordField().getPassword();
        String confirmPassword = registerView.getConfirmPasswordField().getPassword();

        setLoadingState(true);

        RegisterRequest registerRequest = new RegisterRequest(email, password, confirmPassword);
        Call<GenericResponse<String>> call = apiService.register(registerRequest);

        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        clearFields();
                        registerView.getRegisterButton().setDisable(true);
                        showSuccess(response.body().getMessage());
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();

                            showError("Something went wrong!\n " + errorMessage);
                        } catch (Exception e) {
                            showError("Something went wrong!\n" + response.message());
                        }
                    }
                    setLoadingState(false);
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

    private void clearFields() {
        registerView.getEmailField().clear();
        registerView.getPasswordField().clear();
        registerView.getConfirmPasswordField().clear();
    }

    private void setLoadingState(boolean loading) {
        registerView.getRegisterButton().setDisable(loading);
    }

    private void showError(String message) {
        new CustomNotification(
                registerView.getRootContainer(),
                message,
                Styles.DANGER).showNotification();
    }

    private void showSuccess(String message) {
        new CustomNotification(
                registerView.getRootContainer(),
                message).showNotification();
    }

}
