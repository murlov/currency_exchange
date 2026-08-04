package ru.murlov.dto;

import java.math.BigDecimal;

public record ExchangeResponse(CurrencyResponse base_currency,
                               CurrencyResponse target_currency,
                               BigDecimal rate,
                               BigDecimal amount,
                               BigDecimal convertedAmount) {
}
