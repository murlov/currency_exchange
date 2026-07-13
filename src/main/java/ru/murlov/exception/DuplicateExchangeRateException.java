package ru.murlov.exception;

public class DuplicateExchangeRateException extends RuntimeException {
    public DuplicateExchangeRateException(String message) {
        super(message);
    }
}
