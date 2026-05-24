package ru.murlov.exceptions;

public class DuplicateCurrencyCodeException extends RuntimeException {
    public DuplicateCurrencyCodeException(String message) {
        super(message);
    }
}
