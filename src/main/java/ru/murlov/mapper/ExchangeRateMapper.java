package ru.murlov.mapper;

import ru.murlov.dto.ExchangeRateDto;
import ru.murlov.model.ExchangeRate;

public class ExchangeRateMapper {

    public static ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(
                exchangeRate.getId(),
                exchangeRate.getBase_currency(),
                exchangeRate.getTarget_currency(),
                exchangeRate.getRate()
        );
    }

    public static ExchangeRate toModel(ExchangeRateDto exchangeRateDto) {
        return new ExchangeRate(
                exchangeRateDto.getBase_currency(),
                exchangeRateDto.getTarget_currency(),
                exchangeRateDto.getRate()
        );
    }
}
