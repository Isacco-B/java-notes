package com.isaccobertoli.views.note;

import java.util.HashMap;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;
import org.kordamp.ikonli.materialdesign2.MaterialDesignN;

import com.isaccobertoli.components.AlertDialog;
import com.isaccobertoli.components.NoteCard;
import com.isaccobertoli.dto.note.NoteDTO;
import com.isaccobertoli.interfaces.NoteActionListener;
import com.isaccobertoli.utils.TokenUtils;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class ListNoteView {
    private static final int MAX_WIDTH = 1300;
    private StackPane rootContainer = new StackPane();
    private ModalPane modalPane = new ModalPane();
    private ProgressIndicator progressIndicator;
    private FlowPane flowPane;
    private Button createNoteBtn;
    private Button logoutButton;
    private NoteActionListener onDeleteListener;
    private NoteActionListener onViewListener;
    private NoteActionListener onEditListener;
    private NoteActionListener onLogoutListener;

    public Scene createScene() {
        BorderPane mainContainer = new BorderPane();
        mainContainer.setMaxWidth(MAX_WIDTH);
        mainContainer.setPadding(new Insets(20));

        BorderPane listNoteContainer = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        flowPane = new FlowPane();
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setPadding(new Insets(10));

        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);

        listNoteContainer.setTop(setupListNoteHeader());
        listNoteContainer.setCenter(scrollPane);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);

        StackPane progressContainer = new StackPane(listNoteContainer, progressIndicator);
        progressContainer.setAlignment(Pos.CENTER);

        mainContainer.setTop(setupUserHeader());
        mainContainer.setCenter(progressContainer);

        rootContainer.getChildren().addAll(mainContainer, modalPane);
        rootContainer.getStyleClass().add(Styles.BG_INSET);

        return new Scene(rootContainer);
    }

    private HBox setupUserHeader() {
        HashMap<String, String> userData = TokenUtils.readTokenPayload();
        HBox headerContainer = new HBox();
        Tile userInfo = new Tile();
        userInfo.setTitle("ID: " + (userData != null ? userData.get("userId") : "unknown"));
        userInfo.setDescription("Email: " + (userData != null ? userData.get("email") : "unknown"));

        Spacer headerSpacer = new Spacer();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        logoutButton = new Button(null, new FontIcon(MaterialDesignL.LOGOUT));
        logoutButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE);

        logoutButton.setOnAction(v -> {
            AlertDialog logoutDialog = setupLogoutDialog();
            logoutDialog.setOnConfirm(event -> {
                if (onLogoutListener != null) {
                    onLogoutListener.onLogout();
                }
                modalPane.hide();
            });

            logoutDialog.setOnCancel(event -> modalPane.hide());
            modalPane.show(logoutDialog.getDialogContent());
        });

        headerContainer.setAlignment(Pos.CENTER);
        headerContainer.getChildren().addAll(userInfo, headerSpacer, logoutButton);
        headerContainer.setPadding(new Insets(10, 0, 60, 0));
        return headerContainer;
    }

    private HBox setupListNoteHeader() {
        HBox header = new HBox();

        Label headerLogo = new Label("Your Notes");
        headerLogo.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.TITLE_1);

        createNoteBtn = new Button("Add Note", new FontIcon(MaterialDesignN.NOTE_PLUS_OUTLINE));
        createNoteBtn.setContentDisplay(ContentDisplay.RIGHT);
        createNoteBtn.setDefaultButton(true);
        createNoteBtn.getStyleClass().addAll(Styles.LARGE);

        Spacer spacer = new Spacer();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(headerLogo, spacer, createNoteBtn);
        header.setPadding(new Insets(0, 0, 20, 0));
        return header;
    }

    private AlertDialog setupLogoutDialog() {
        AlertDialog logoutDialog = new AlertDialog("Logout Confirmation", "Are you sure you want to logout?");

        return logoutDialog;
    }

    private AlertDialog setupDeleteDialog(String noteTitle) {
        String message = "Are you sure you want to delete \"" + noteTitle + "\"?\nThis action cannot be undone.";
        AlertDialog deleteDialog = new AlertDialog("Delete Confirmation", message);

        return deleteDialog;
    }

    public void updateNotes(List<NoteDTO> notes) {
        flowPane.getChildren().clear();
        if (notes != null && !notes.isEmpty()) {
            for (NoteDTO note : notes) {
                NoteCard noteCard = new NoteCard(note.getTitle(), note.getContent(), note.getCreatedAt(),
                        note.getUpdatedAt());

                noteCard.setOnView(v -> {
                    if (onViewListener != null) {
                        onViewListener.onView(note.getNoteId());
                    }
                });

                noteCard.setOnEdit(v -> {
                    if (onEditListener != null) {
                        onEditListener.onEdit(note.getNoteId());
                    }
                });

                noteCard.setOnDelete(v -> {
                    AlertDialog deleteDialog = setupDeleteDialog(note.getTitle());

                    deleteDialog.setOnConfirm(confirmEvent -> {
                        if (onDeleteListener != null) {
                            onDeleteListener.onDelete(note.getNoteId());
                        }
                        modalPane.hide();
                    });

                    deleteDialog.setOnCancel(cancelEvent -> modalPane.hide());

                    modalPane.show(deleteDialog.getDialogContent());
                });

                flowPane.getChildren().add(noteCard);
            }
        } else {
            Label errorMessage = new Label("No notes found");
            errorMessage.getStyleClass().add(Styles.TEXT_MUTED);
            flowPane.getChildren().add(errorMessage);
        }
    }

    public void showLoader(boolean visible) {
        progressIndicator.setVisible(visible);
    }

    public Button getCreateNoteButton() {
        return createNoteBtn;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void setNoteActionListener(NoteActionListener listener) {
        this.onDeleteListener = listener;
        this.onViewListener = listener;
        this.onEditListener = listener;
    }

    public void setLogoutListener(NoteActionListener listener) {
        this.onLogoutListener = listener;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
