package com.isaccobertoli.controllers.note;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.note.CreateNoteRequest;
import com.isaccobertoli.dto.note.NoteDTO;
import com.isaccobertoli.dto.note.NoteDetailResponse;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.views.note.UpdateNoteView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNoteController {
    private final String noteId;
    private final UpdateNoteView updateNoteView;
    private final ApiService apiService = RetrofitClient.getService();

    public UpdateNoteController(String noteId) {
        this.noteId = noteId;
        this.updateNoteView = new UpdateNoteView();
    }

    public Scene getScene() {
        Scene updateNoteScene = updateNoteView.createScene();
        initialize();
        return updateNoteScene;
    }

    private void initialize() {
        fetchNoteDetails();

        updateNoteView.getUpdateButton().setOnAction(e -> handleUpdateNote());
        updateNoteView.getCancelButton().setOnAction(e -> SceneManager.navigateToListNote());
        updateNoteView.getGoBackBtn().setOnAction(e -> SceneManager.navigateToListNote());

        Platform.runLater(() -> {
            Stage stage = (Stage) updateNoteView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void fetchNoteDetails() {
        Call<GenericResponse<NoteDetailResponse>> call = apiService.getNoteById(noteId);
        call.enqueue(new Callback<GenericResponse<NoteDetailResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<NoteDetailResponse>> call,
                    Response<GenericResponse<NoteDetailResponse>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        NoteDTO note = response.body().getData().getNote();
                        updateNoteView.updateNoteDetails(note);
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();
                            updateNoteView.showErrorCard();
                            showError("Something went wrong!\n" + errorMessage);
                        } catch (Exception e) {
                            updateNoteView.showErrorCard();
                            showError("Something went wrong!\n" + response.message());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<GenericResponse<NoteDetailResponse>> call, Throwable t) {
                Platform.runLater(() -> {
                    showError("Something went wrong!\n " + t.getMessage());
                });
            }
        });
    }

    private void handleUpdateNote() {
        String title = updateNoteView.getTitleField().getText();
        String content = updateNoteView.getContentField().getText();

        setLoadingState(true);

        CreateNoteRequest createNoteRequest = new CreateNoteRequest(title, content);
        Call<GenericResponse<String>> call = apiService.updateNote(noteId, createNoteRequest);

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
        updateNoteView.getUpdateButton().setDisable(loading);
    }

    private void showError(String message) {
        new CustomNotification(
                updateNoteView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }
}
