package ru.murlov.mapper;

import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.model.ExchangeRate;

public class ExchangeRateMapper {

    public static ExchangeRateResponse toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(
                exchangeRate.getId(),
                CurrencyMapper.toDto(exchangeRate.getBase_currency()),
                CurrencyMapper.toDto(exchangeRate.getTarget_currency()),
                exchangeRate.getRate()
        );
    }
}
