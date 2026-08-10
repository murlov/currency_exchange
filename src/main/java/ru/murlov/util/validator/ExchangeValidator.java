package ru.murlov.util.validator;

import ru.murlov.dto.ExchangeRequest;
import ru.murlov.exception.ValidationException;

public final class ExchangeValidator {
    public static void validate(ExchangeRequest exchangeRequest) {
        validateBaseCurrencyCode(exchangeRequest.baseCurrencyCode());
        validateTargetCurrencyCode(exchangeRequest.targetCurrencyCode());
    }

    private static void validateBaseCurrencyCode(String baseCurrencyCode) {
        requireNotBlank(baseCurrencyCode, "Base currency code is required");

        if (!baseCurrencyCode.matches("[A-Z]{3}")) {
            throw new ValidationException("Base currency code must contain exactly 3 uppercase letters");
        }
    }

    private static void validateTargetCurrencyCode(String targetCurrencyCode) {
        requireNotBlank(targetCurrencyCode, "Target currency code is required");

        if (!targetCurrencyCode.matches("[A-Z]{3}")) {
            throw new ValidationException("Target currency code must contain exactly 3 uppercase letters");
        }
    }

    private static void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }
}
