package ru.murlov.util;

import ru.murlov.dto.CurrencyDto;
import ru.murlov.exception.ValidationException;

public final class CurrencyValidator {

    private CurrencyValidator() {}

    public static void validate(CurrencyDto currencyDto) {
        validateName(currencyDto.getName());
        validateCode(currencyDto.getCode());
        validateSign(currencyDto.getSign());
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
