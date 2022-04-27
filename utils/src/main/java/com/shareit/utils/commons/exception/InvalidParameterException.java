package com.shareit.utils.commons.exception;

public class InvalidParameterException extends BadRequestException {
    private String paramName;

    public InvalidParameterException(String paramName) {
        super(String.format("Invalid param: %s", paramName));
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
