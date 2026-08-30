package ru.murlov.mapper;

import ru.murlov.dto.ExchangeResponse;
import ru.murlov.model.Exchange;

public final class ExchangeMapper {

    private ExchangeMapper() {}

    public static ExchangeResponse toDto(Exchange exchange) {
        return new ExchangeResponse(
                CurrencyMapper.toDto(exchange.baseCurrency()),
                CurrencyMapper.toDto(exchange.targetCurrency()),
                exchange.rate(),
                exchange.amount(),
                exchange.convertedAmount()
        );
    }
}
