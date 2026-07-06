package ru.murlov.util;

import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.exception.ValidationException;

public final class CurrencyValidator {

    private CurrencyValidator() {}

    public static void validate(CurrencyCreateRequest currencyCreateRequest) {
        validateName(currencyCreateRequest.name());
        validateCode(currencyCreateRequest.code());
        validateSign(currencyCreateRequest.sign());
    }

    private static void validateName(String name) {
        requireNotBlank(name, "Currency name is required");

        if (name.length() > 30) {
            throw new ValidationException("Currency name is too long");
        }
    }

    private static void validateCode(String code) {
        requireNotBlank(code, "Currency code is required");

        if (!code.matches("[A-Z]{3}")) {
            throw new ValidationException("Currency code must contain exactly 3 letters");
        }
    }

    private static void validateSign(String sign) {
        if (sign.length() > 5) {
            throw new ValidationException("Currency sign is too long");
        }
    }

    private static void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }
}
