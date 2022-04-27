package com.shareit.infrastructure.upload;

public class UploadError {
    private String fileName;
    private String errorMsg;

    public UploadError(String fileName, String errorMsg) {
        this.fileName = fileName;
        this.errorMsg = errorMsg;
    }

    public String getFileName() {
        return fileName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}