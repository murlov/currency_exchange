package ru.murlov.mapper;

import ru.murlov.dto.CurrencyResponse;
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

    public static ExchangeRate toModel(CurrencyResponse baseCurrencyResponse,
                                       CurrencyResponse targetCurrencyResponse,
                                       Float rate) {
                return new ExchangeRate(CurrencyMapper.toModel(baseCurrencyResponse),
                        CurrencyMapper.toModel(targetCurrencyResponse),
                        rate
        );
    }
}
