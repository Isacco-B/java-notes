package com.isaccobertoli.exceptions.noteExceptions;

import com.isaccobertoli.exceptions.BaseException;

public class NoteNotFoundException extends BaseException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}
