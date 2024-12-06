package com.isaccobertoli.interfaces;

public interface NoteActionListener {
    void onDelete(String noteId);

    void onEdit(String noteId);

    void onView(String noteId);

    void onLogout();
}
