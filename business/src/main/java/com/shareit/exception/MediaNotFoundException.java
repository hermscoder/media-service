package com.shareit.exception;

import com.shareit.utils.commons.exception.BadRequestException;

public class MediaNotFoundException extends BadRequestException {
    public MediaNotFoundException(String message) {
        super(message);
    }
}
