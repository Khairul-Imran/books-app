package com.example.books_app_backend.Exceptions;

public class BookDataParsingException extends RuntimeException {
    public BookDataParsingException(String message) {
        super(message);
    }

    public BookDataParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
