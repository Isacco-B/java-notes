package com.isaccobertoli.controllers.note;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.note.NoteDTO;
import com.isaccobertoli.dto.note.NoteDetailResponse;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.views.note.DetailNoteView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailNoteController {
    private final String noteId;
    private final DetailNoteView detailNoteView;
    private final ApiService apiService = RetrofitClient.getService();

    public DetailNoteController(String noteId) {
        this.noteId = noteId;
        this.detailNoteView = new DetailNoteView();
    }

    public Scene getScene() {
        Scene detailNoteScene = detailNoteView.createScene();
        initialize();
        return detailNoteScene;
    }

    private void initialize() {
        fetchNoteDetails();

        detailNoteView.getGoBackBtn().setOnAction(e -> SceneManager.navigateToListNote());

        Platform.runLater(() -> {
            Stage stage = (Stage) detailNoteView.getRootContainer().getScene().getWindow();
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
                        detailNoteView.updateNoteDetails(note);
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();
                            detailNoteView.showErrorCard();
                            showError("Something went wrong!\n" + errorMessage);
                        } catch (Exception e) {
                            detailNoteView.showErrorCard();
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

    private void showError(String message) {
        new CustomNotification(
                detailNoteView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }
}
