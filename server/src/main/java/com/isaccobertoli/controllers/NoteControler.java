package com.isaccobertoli.controllers;

import java.util.List;
import java.util.Map;

import com.isaccobertoli.dto.NoteDTO;
import com.isaccobertoli.exceptions.noteExceptions.NoteNotFoundException;
import com.isaccobertoli.exceptions.noteExceptions.NoteValidationException;
import com.isaccobertoli.models.NoteModel;
import com.isaccobertoli.schemas.NoteValidationSchema;
import com.isaccobertoli.services.NoteService;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;

public class NoteControler {
    private final NoteService noteService;

    public NoteControler(NoteService noteService) {
        this.noteService = noteService;
    }

    public void getAllNotes(Context ctx) {
        try {
            String author = ctx.attribute("userId");
            List<NoteModel> notes = noteService.getAllNotes(author);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "data", Map.of("notes", notes));
            ctx.json(response);
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void getNoteById(Context ctx) {
        try {
            String noteId = ctx.pathParam("noteId");
            String author = ctx.attribute("userId");
            NoteModel note = noteService.getNoteById(noteId, author);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "data", Map.of("notes", note));
            ctx.json(response);
        } catch (NoteValidationException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (NoteNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void createNote(Context ctx) {
        try {
            String author = ctx.attribute("userId");
            NoteDTO note = ctx.bodyAsClass(NoteDTO.class);
            note.setAuthor(author);
            NoteValidationSchema.CreateSchema(note);
            noteService.createNote(note);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "Note created successfully");
            ctx.json(response);
        } catch (NoteValidationException e) {
            Map<String, Object> response = Map.of(
                    "status", "error",
                    "message", Map.of("error", e.getMessage().split(";")),
                    "code", 400);
            ctx.json(response).status(400);
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void updateNote(Context ctx) {
        try {
            String noteId = ctx.pathParam("noteId");
            String author = ctx.attribute("userId");
            NoteDTO note = ctx.bodyAsClass(NoteDTO.class);
            NoteValidationSchema.UpdateSchema(note);
            noteService.updateNote(noteId, note, author);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "Note updated successfully");
            ctx.json(response);
        } catch (NoteValidationException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (NoteNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void deleteNote(Context ctx) {
        try {
            String noteId = ctx.pathParam("noteId");
            String author = ctx.attribute("userId");
            noteService.deleteNote(noteId, author);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "Note deleted successfully");
            ctx.json(response);
        } catch (NoteValidationException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (NoteNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }
}
