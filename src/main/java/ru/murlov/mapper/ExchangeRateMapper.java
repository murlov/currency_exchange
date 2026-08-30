package ru.murlov.mapper;

import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.model.ExchangeRate;


public final class ExchangeRateMapper {

    private ExchangeRateMapper() {}

    public static ExchangeRateResponse toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(
                exchangeRate.getId(),
                CurrencyMapper.toDto(exchangeRate.getBaseCurrency()),
                CurrencyMapper.toDto(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate()
        );
    }
}
