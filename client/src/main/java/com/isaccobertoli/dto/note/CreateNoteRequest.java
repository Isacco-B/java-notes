package com.isaccobertoli.dto.note;

public class CreateNoteRequest {
    private String title;
    private String content;

    public CreateNoteRequest() {}

    public CreateNoteRequest(String title, String content) {
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
}
