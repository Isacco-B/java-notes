package com.isaccobertoli.controllers.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.auth.LoginRequest;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.utils.TokenStorage;
import com.isaccobertoli.views.auth.LoginView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {
    private final LoginView loginView;
    private final ApiService apiService = RetrofitClient.createService();

    public LoginController() {
        this.loginView = new LoginView();
    }

    public Scene getScene() {
        Scene loginScene = loginView.createScene();
        initialize();
        return loginScene;

    }

    private void initialize() {
        loginView.getRegisterLink().setOnAction(e -> SceneManager.navigateToSignUp());
        loginView.getForgotPasswordLink().setOnAction(e -> SceneManager.navigateToResetPassword());
        loginView.getLoginButton().setOnAction(e -> handleLogin());

        Platform.runLater(() -> {
            Stage stage = (Stage) loginView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void handleLogin() {
        String email = loginView.getEmailField().getText();
        String password = loginView.getPasswordField().getPassword();

        setLoadingState(true);

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<GenericResponse<String>> call = apiService.login(loginRequest);

        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        String accessToken = response.body().getMessage();
                        TokenStorage.saveToken(accessToken);
                        SceneManager.navigateToListNote();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();

                            showError("Something went wrong!\n" + errorMessage);
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

    private void setLoadingState(boolean loading) {
        loginView.getLoginButton().setDisable(loading);
    }

    private void showError(String message) {
        new CustomNotification(
                loginView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }
}
