package ru.murlov.currency;

public class CurrencyMapper {

    public static Currency toModel(CurrencyDto dto) {
        Currency currency = new Currency();
        currency.setCode(dto.getCode());
        currency.setName(dto.getName());
        currency.setSign(dto.getSign());
        return currency;
    }

    public static CurrencyDto toDto(Currency currency) {
        CurrencyDto dto = new CurrencyDto(currency.getId());
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        dto.setSign(currency.getSign());
        return dto;
    }
}
