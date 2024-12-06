package com.isaccobertoli.exceptions.userExceptions;

import com.isaccobertoli.exceptions.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
