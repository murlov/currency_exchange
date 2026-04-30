package ru.murlov.currency;

public class CurrencyMapper {

    public static Currency toModel(CurrencyDto dto) {
        Currency currency = new Currency();
        currency.setCode(dto.getCode());
        currency.setFullName(dto.getFullName());
        currency.setSign(dto.getSign());
        return currency;
    }

    public static CurrencyDto toDto(Currency currency) {
        CurrencyDto dto = new CurrencyDto();
        dto.setCode(currency.getCode());
        dto.setFullName(currency.getFullName());
        dto.setSign(currency.getSign());
        return dto;
    }
}
