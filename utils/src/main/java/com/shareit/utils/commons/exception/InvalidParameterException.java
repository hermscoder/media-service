package com.shareit.utils.commons.exception;

public class InvalidParameterException extends BadRequestException {
    private String paramName;

    public InvalidParameterException(String paramName) {
        this(paramName, null);
    }

    public InvalidParameterException(String paramName, String message) {
        super(message == null
                ? String.format("Invalid param: %s", paramName)
                : String.format(message, paramName));
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
