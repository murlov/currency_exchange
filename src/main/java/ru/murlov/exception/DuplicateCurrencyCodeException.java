package ru.murlov.exception;

public class DuplicateCurrencyCodeException extends RuntimeException {
    public DuplicateCurrencyCodeException(String message) {
        super(message);
    }
}
