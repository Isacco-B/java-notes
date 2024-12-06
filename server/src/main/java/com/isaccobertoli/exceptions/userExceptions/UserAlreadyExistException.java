package com.isaccobertoli.exceptions.userExceptions;

import com.isaccobertoli.exceptions.BaseException;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
