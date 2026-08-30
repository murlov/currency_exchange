package ru.murlov.mapper;

import ru.murlov.dto.CurrencyResponse;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.model.ExchangeRate;

import java.math.BigDecimal;

public final class ExchangeRateMapper {

    private ExchangeRateMapper () {}

    public static ExchangeRateResponse toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(
                exchangeRate.getId(),
                CurrencyMapper.toDto(exchangeRate.getBaseCurrency()),
                CurrencyMapper.toDto(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate()
        );
    }

    public static ExchangeRate toModel(CurrencyResponse baseCurrencyResponse,
                                       CurrencyResponse targetCurrencyResponse,
                                       BigDecimal rate) {
                return new ExchangeRate(CurrencyMapper.toModel(baseCurrencyResponse),
                        CurrencyMapper.toModel(targetCurrencyResponse),
                        rate
        );
    }
}
