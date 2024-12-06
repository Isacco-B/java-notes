package com.isaccobertoli.services;

import java.util.List;

import com.isaccobertoli.dto.NoteDTO;
import com.isaccobertoli.exceptions.noteExceptions.NoteNotFoundException;
import com.isaccobertoli.exceptions.noteExceptions.NoteValidationException;
import com.isaccobertoli.models.NoteModel;
import com.isaccobertoli.repository.NoteRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;

public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteModel> getAllNotes(String author) {
        return noteRepository.getAll(author);
    }

    public NoteModel getNoteById(String id, String author) throws NoteNotFoundException, NoteValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw new NoteValidationException("Note ID is required");
        }
        NoteModel note = noteRepository.getById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if (!note.getAuthor().equals(author)) {
            throw new ForbiddenResponse("You are not allowed");
        }

        return note;
    }

    public void createNote(NoteDTO note) {
        NoteModel newNote = new NoteModel(note.getTitle(), note.getContent(), note.getAuthor());
        InsertOneResult result = noteRepository.save(newNote);

        if (result.getInsertedId() == null) {
            throw new InternalServerErrorResponse("Failed to create note");
        }
    }

    public void updateNote(String id, NoteDTO note, String author)
            throws NoteValidationException, NoteNotFoundException {
        if (id == null || id.trim().isEmpty()) {
            throw new NoteValidationException("Note ID is required");
        }

        NoteModel existingNote = noteRepository.getById(id).orElse(null);

        if (existingNote == null) {
            throw new NoteNotFoundException("Note not found");
        }

        if (!existingNote.getAuthor().equals(author)) {
            throw new ForbiddenResponse("You are not allowed");
        }

        NoteModel updatedNote = new NoteModel(note.getTitle(), note.getContent(), note.getAuthor());
        UpdateResult result = noteRepository.update(id, updatedNote);

        if (result.getModifiedCount() == 0) {
            throw new InternalServerErrorResponse("Failed to update note");
        }
    }

    public void deleteNote(String id, String author) throws NoteNotFoundException, NoteValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw new NoteValidationException("Note ID is required");
        }

        NoteModel existingNote = noteRepository.getById(id).orElse(null);

        if (existingNote == null) {
            throw new NoteNotFoundException("Note not found");
        }

        if (!existingNote.getAuthor().equals(author)) {
            throw new ForbiddenResponse("You are not allowed");
        }

        DeleteResult result = noteRepository.delete(id);

        if (result.getDeletedCount() == 0) {
            throw new InternalServerErrorResponse("Failed to delete note");
        }
    }
}
