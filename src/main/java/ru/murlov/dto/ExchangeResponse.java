package ru.murlov.dto;

public record ExchangeResponse(CurrencyResponse base_currency,
                               CurrencyResponse target_currency,
                               float rate,
                               float amount,
                               float convertedAmount) {
}
