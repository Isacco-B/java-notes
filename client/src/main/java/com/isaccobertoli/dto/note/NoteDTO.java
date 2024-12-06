package com.isaccobertoli.dto.note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDTO {
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private String updatedAt;
    private String noteId;

    public NoteDTO() {
    }

    public NoteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        if (createdAt != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = inputFormat.parse(createdAt);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return outputFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                return createdAt;
            }
        }
        return null;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        if (updatedAt != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = inputFormat.parse(updatedAt);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return outputFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                return updatedAt;
            }
        }
        return null;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
