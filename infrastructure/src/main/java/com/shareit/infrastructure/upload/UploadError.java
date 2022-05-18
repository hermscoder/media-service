package com.shareit.infrastructure.upload;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadError that = (UploadError) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(errorMsg, that.errorMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, errorMsg);
    }

    @Override
    public String toString() {
        return "UploadError{" +
                "fileName='" + fileName + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}