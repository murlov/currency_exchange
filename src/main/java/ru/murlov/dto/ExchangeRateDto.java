package ru.murlov.dto;

import ru.murlov.model.Currency;

public record ExchangeRateDto (int id,
                               Currency base_currency,
                               Currency target_currency,
                               float rate) {
}
