package ru.murlov.mapper;

import ru.murlov.dto.CurrencyDto;
import ru.murlov.model.Currency;

public class CurrencyMapper {

    public static Currency toModel(CurrencyDto dto) {
        return new Currency(
                dto.getCode(),
                dto.getName(),
                dto.getSign()
        );
    }

    public static CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                currency.getSign()
        );
    }
}
