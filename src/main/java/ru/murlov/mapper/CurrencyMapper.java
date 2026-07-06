package ru.murlov.mapper;

import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.model.Currency;

public class CurrencyMapper {

    public static Currency toModel(CurrencyCreateRequest currencyCreateRequest) {
        return new Currency(
                currencyCreateRequest.code(),
                currencyCreateRequest.name(),
                currencyCreateRequest.sign()
        );
    }

    public static CurrencyResponse toDto(Currency currency) {
        return new CurrencyResponse(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                currency.getSign()
        );
    }
}
