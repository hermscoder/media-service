package com.shareit.infrastructure.upload.exception;

import com.shareit.utils.commons.exception.InternalErrorException;

public class FailedDestructionException extends InternalErrorException {
    public FailedDestructionException(String message) {
        super(message);
    }
}
