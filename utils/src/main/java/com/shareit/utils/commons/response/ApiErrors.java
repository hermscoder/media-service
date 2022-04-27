package com.shareit.utils.commons.response;

import java.util.ArrayList;
import java.util.List;


public class ApiErrors {
    private List<Error> errors;

    public ApiErrors() {
        this.errors = new ArrayList<>();
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void addError(Error error){
        errors.add(error);
    }
}
