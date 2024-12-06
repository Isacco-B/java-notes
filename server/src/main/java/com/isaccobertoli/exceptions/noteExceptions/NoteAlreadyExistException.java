package com.isaccobertoli.exceptions.noteExceptions;

import com.isaccobertoli.exceptions.BaseException;

public class NoteAlreadyExistException extends BaseException {
    public NoteAlreadyExistException(String message) {
        super(message);
    }
}
