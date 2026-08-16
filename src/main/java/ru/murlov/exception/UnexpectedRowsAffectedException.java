package ru.murlov.exception;

public class UnexpectedRowsAffectedException extends RuntimeException {
    public UnexpectedRowsAffectedException(String message) {
        super(message);
    }
}
