package com.isaccobertoli.dto.note;

import java.util.List;

public class NoteListResponse {
    private List<NoteDTO> notes;

    public List<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDTO> notes) {
        this.notes = notes;
    }
}
