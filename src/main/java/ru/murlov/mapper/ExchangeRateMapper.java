package ru.murlov.mapper;

import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.model.ExchangeRate;

public class ExchangeRateMapper {

    public static ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(
    public static ExchangeRateResponse toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(
                exchangeRate.getId(),
                CurrencyMapper.toDto(exchangeRate.getBase_currency()),
                CurrencyMapper.toDto(exchangeRate.getTarget_currency()),
                exchangeRate.getRate()
        );
    }

    public static ExchangeRate toModel(ExchangeRateDto exchangeRateDto) {
        return new ExchangeRate(
                exchangeRateDto.base_currency(),
                exchangeRateDto.target_currency(),
                exchangeRateDto.rate()
        );
    }
}
