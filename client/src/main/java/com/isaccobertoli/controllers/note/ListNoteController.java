package com.isaccobertoli.controllers.note;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isaccobertoli.components.CustomNotification;
import com.isaccobertoli.controllers.auth.LoginController;
import com.isaccobertoli.dto.GenericResponse;
import com.isaccobertoli.dto.note.NoteDTO;
import com.isaccobertoli.dto.note.NoteListResponse;
import com.isaccobertoli.interfaces.NoteActionListener;
import com.isaccobertoli.services.ApiService;
import com.isaccobertoli.utils.RetrofitClient;
import com.isaccobertoli.utils.SceneManager;
import com.isaccobertoli.utils.TokenStorage;
import com.isaccobertoli.views.note.ListNoteView;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNoteController implements NoteActionListener {
    private final ListNoteView noteListView;
    private final ApiService apiService = RetrofitClient.createService();

    public ListNoteController() {
        this.noteListView = new ListNoteView();
        this.noteListView.setNoteActionListener(this);
        this.noteListView.setLogoutListener(this);
    }

    public Scene getScene() {
        Scene noteListScene = noteListView.createScene();
        initialize();
        return noteListScene;
    }

    private void initialize() {
        fetchNotes();

        noteListView.getCreateNoteButton().setOnAction(e -> SceneManager.navigateToCreateNote());
        Platform.runLater(() -> {
            Stage stage = (Stage) noteListView.getRootContainer().getScene().getWindow();
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });
    }

    private void handleLogOut() {
        TokenStorage.clearToken();
        Scene loginScene = new LoginController().getScene();
        SceneManager.switchScene(loginScene, "Login");
    }

    private void fetchNotes() {
        Call<GenericResponse<NoteListResponse>> call = apiService.getNotes();
        call.enqueue(new Callback<GenericResponse<NoteListResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<NoteListResponse>> call,
                    Response<GenericResponse<NoteListResponse>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        List<NoteDTO> notes = response.body().getData().getNotes();
                        noteListView.updateNotes(notes);
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JsonObject jsonError = new Gson().fromJson(errorBody, JsonObject.class);
                            String errorMessage = jsonError.get("message").getAsString();

                            showError("Something went wrong!\n" + errorMessage);
                        } catch (Exception e) {
                            showError("Something went wrong!\n" + response.message());
                        }
                        List<NoteDTO> notes = null;
                        noteListView.updateNotes(notes);
                    }
                });
            }

            @Override
            public void onFailure(Call<GenericResponse<NoteListResponse>> call, Throwable t) {
                Platform.runLater(() -> showError("Something went wrong!\n " + t.getMessage()));
            }
        });
    }

    @Override
    public void onLogout() {
        handleLogOut();
    }

    @Override
    public void onDelete(String noteId) {
        Call<GenericResponse<String>> call = apiService.deleteNote(noteId);
        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        fetchNotes();
                        showSuccess(response.body().getMessage());
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
                });
            }

            @Override
            public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                Platform.runLater(() -> showError("Something went wrong!\n " + t.getMessage()));
            }
        });
    }

    @Override
    public void onEdit(String noteId) {
        SceneManager.navigateToEditNote(noteId);
    }

    @Override
    public void onView(String noteId) {
        SceneManager.navigateToDetailNote(noteId);
    }

    private void showError(String message) {
        new CustomNotification(
                noteListView.getRootContainer(),
                message, Styles.DANGER).showNotification();
    }

    private void showSuccess(String message) {
        new CustomNotification(
                noteListView.getRootContainer(),
                message).showNotification();
    }

}
