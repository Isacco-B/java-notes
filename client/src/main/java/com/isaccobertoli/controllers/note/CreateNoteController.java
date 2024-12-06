package com.isaccobertoli.controllers.note;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.note.CreateNoteRequest;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.views.note.CreateNoteView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNoteController {
    private final CreateNoteView createNoteView;
    private final ApiService apiService = RetrofitClient.getService();

    public CreateNoteController() {
        this.createNoteView = new CreateNoteView();
    }

    public Scene getScene() {
        Scene createNoteScene = createNoteView.createScene();
        initialize();
        return createNoteScene;

    }

    private void initialize() {
        createNoteView.getCancelButton().setOnAction(e -> SceneManager.navigateToListNote());
        createNoteView.getCreateButton().setOnAction(e -> handleCreateNote());

        Platform.runLater(() -> {
            Stage stage = (Stage) createNoteView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void handleCreateNote() {
        String title = createNoteView.getTitleField().getText();
        String content = createNoteView.getContentField().getText();

        setLoadingState(true);

        CreateNoteRequest createNoteRequest = new CreateNoteRequest(title, content);
        Call<GenericResponse<String>> call = apiService.createNote(createNoteRequest);

        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
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
        createNoteView.getCreateButton().setDisable(loading);
    }

    private void showError(String message) {
        new CustomNotification(
                createNoteView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }
}
