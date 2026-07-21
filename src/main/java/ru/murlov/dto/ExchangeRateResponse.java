package ru.murlov.dto;

    public record ExchangeRateResponse (int id,
                                        CurrencyResponse base_currency,
                                        CurrencyResponse target_currency,
                                        float rate) {
}
