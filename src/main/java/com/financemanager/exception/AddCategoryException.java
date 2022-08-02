package com.financemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Category already exists in user list")
public class AddCategoryException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public AddCategoryException(String message) {
        super(message);
    }
    
    public AddCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
