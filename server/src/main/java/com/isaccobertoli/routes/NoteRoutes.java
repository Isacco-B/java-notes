package com.isaccobertoli.routes;

import static io.javalin.apibuilder.ApiBuilder.*;

import com.isaccobertoli.controllers.NoteControler;
import com.isaccobertoli.middlewares.AuthMiddleware;

public class NoteRoutes {
    public static void configure(NoteControler noteControler) {
        path("/notes", () -> {
            before(ctx -> AuthMiddleware.verifyToken(ctx));
            get(noteControler::getAllNotes);
            post(noteControler::createNote);
            path("/{noteId}", () -> {
                get(noteControler::getNoteById);
                put(noteControler::updateNote);
                delete(noteControler::deleteNote);
            });
        });
    }
}
