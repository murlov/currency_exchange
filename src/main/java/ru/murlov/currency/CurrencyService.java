package ru.murlov.currency;

import ru.murlov.exceptions.NotFoundException;

public class CurrencyService {

    public CurrencyDto getByCode(String code) {
        CurrencyDao dao = new CurrencyDao();
        Currency currency = dao.getByCode(code).orElseThrow(() -> new NotFoundException("Currency not found: " + code));
        return CurrencyMapper.toDto(currency);
    }
}
