package com.shareit.infrastructure.upload.exception;

import com.shareit.infrastructure.upload.UploadError;
import com.shareit.utils.commons.exception.InternalErrorException;

import java.util.Collections;
import java.util.List;

public class FailedUploadException extends InternalErrorException {
    List<UploadError> uploadErrors;

    public FailedUploadException(String message) {
        this(message, Collections.emptyList());

    }
    public FailedUploadException(String message, List<UploadError> uploadErrors) {
        super(message);
        this.uploadErrors = uploadErrors;
    }

    public List<UploadError> getUploadErrors() {
        return uploadErrors;
    }
}
