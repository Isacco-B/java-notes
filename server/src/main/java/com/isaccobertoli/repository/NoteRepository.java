package com.isaccobertoli.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.isaccobertoli.models.NoteModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class NoteRepository {
    private final MongoCollection<NoteModel> noteCollection;

    public NoteRepository(MongoDatabase database) {
        noteCollection = database.getCollection("notes", NoteModel.class);
    }

    public InsertOneResult save(NoteModel note) {
        InsertOneResult result = noteCollection.insertOne(note);
        return result;
    }

    public List<NoteModel> getAll(String author) {
        return noteCollection.find(Filters.eq("author", author)).into(new ArrayList<>());
    }

    public Optional<NoteModel> getById(String noteId) {
        ObjectId id = new ObjectId(noteId);
        return Optional.ofNullable(
                noteCollection.find(Filters.eq("_id", id)).first());
    }

    public Optional<NoteModel> getByTitle(String title) {
        return Optional.ofNullable(
                noteCollection.find(Filters.eq("title", title)).first());
    }

    public UpdateResult update(String noteId, NoteModel note) {
        ObjectId id = new ObjectId(noteId);
        var updateFields = new ArrayList<>();

        if (note.getTitle() != null) {
            updateFields.add(Updates.set("title", note.getTitle()));
        }

        if (note.getContent() != null) {
            updateFields.add(Updates.set("content", note.getContent()));
        }

        updateFields.add(Updates.set("updated_at", note.getUpdatedAt()));

        UpdateResult result = noteCollection.updateOne(
                Filters.eq("_id", id),
                Updates.combine(updateFields.toArray(new Bson[0])));

        return result;
    }

    public DeleteResult delete(String noteId) {
        ObjectId id = new ObjectId(noteId);
        DeleteResult result = noteCollection
                .deleteOne(Filters.eq("_id", id));
        return result;
    }
}
