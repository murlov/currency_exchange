package ru.murlov.util.validator;

import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.CurrencyPair;

public final class ExchangeRateValidator {

    private ExchangeRateValidator() {}

    public static void validate(ExchangeRateRequest exchangeRateRequest) {
        validateBaseCurrencyCode(exchangeRateRequest.baseCurrencyCode());
        validateTargetCurrencyCode(exchangeRateRequest.targetCurrencyCode());
        validateCodesPair(exchangeRateRequest.baseCurrencyCode(), exchangeRateRequest.targetCurrencyCode());
    }

    public static void validate(CurrencyPair currencyPair) {
        validateBaseCurrencyCode(currencyPair.baseCurrencyCode());
        validateTargetCurrencyCode(currencyPair.targetCurrencyCode());
        validateCodesPair(currencyPair.baseCurrencyCode(), currencyPair.targetCurrencyCode());
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

    private static void validateCodesPair(String baseCurrencyCode, String targetCurrencyCode) {
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new ValidationException("Currency codes must be different");
        }
    }

    private static void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }
}
