package com.learning.todo.services.exceptions;

public class DatabaseException extends RuntimeException {  // RuntimeException não exige o uso do Try/Catch

    public DatabaseException(String message) {
        super(message);
    }
}
