package com.isaccobertoli.schemas;

import java.util.ArrayList;
import java.util.List;

import com.isaccobertoli.dto.NoteDTO;
import com.isaccobertoli.exceptions.noteExceptions.NoteValidationException;

public class NoteValidationSchema {

    public static void CreateSchema(NoteDTO dto) throws NoteValidationException {
        List<String> errors = new ArrayList<>();

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            errors.add("Title is required.");
        }

        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            errors.add("Content is required.");
        }

        if (dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
            errors.add("Author is required.");
        }

        if (!errors.isEmpty()) {
            throw new NoteValidationException(String.join(";", errors));
        }
    }

    public static void UpdateSchema(NoteDTO dto) throws NoteValidationException {
        List<String> errors = new ArrayList<>();

        if ((dto.getTitle() == null || dto.getTitle().trim().isEmpty())
                && (dto.getContent() == null || dto.getContent().trim().isEmpty())) {
            errors.add("Title or Content is required.");
        }

        if (!errors.isEmpty()) {
            throw new NoteValidationException(String.join(";", errors));
        }
    }
}
