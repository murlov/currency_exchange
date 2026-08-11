package ru.murlov.dto;

import java.math.BigDecimal;

public record ExchangeRateResponse (Long id,
                                    CurrencyResponse base_currency,
                                    CurrencyResponse target_currency,
                                    BigDecimal rate) {
}
