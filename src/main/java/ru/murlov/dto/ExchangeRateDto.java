package ru.murlov.dto;

import ru.murlov.model.Currency;

public record ExchangeRateDto (Long id,
                               Currency base_currency,
                               Currency target_currency,
                               float rate) {
}
