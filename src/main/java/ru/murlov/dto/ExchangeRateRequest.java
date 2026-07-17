package ru.murlov.dto;

public record ExchangeRateRequest(String baseCurrencyCode,
                                  String targetCurrencyCode,
                                  float rate) {
}
