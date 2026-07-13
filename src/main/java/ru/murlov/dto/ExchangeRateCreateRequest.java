package ru.murlov.dto;

public record ExchangeRateCreateRequest (String baseCurrencyCode,
                                         String targetCurrencyCode,
                                         float rate) {
}
