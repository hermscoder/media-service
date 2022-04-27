package com.shareit.utils.commons.response;

public class Error {
    private String error;
    private String message;
    private String detail;

    public Error(String error, String message, String detail) {
        this.error = error;
        this.message = message;
        this.detail = detail;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
