package ru.murlov.dto;

public record ExchangeRequest(String baseCurrencyCode,
                              String targetCurrencyCode,
                              float amount) {
}
