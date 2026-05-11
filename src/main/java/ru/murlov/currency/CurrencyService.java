package ru.murlov.currency;

import ru.murlov.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {

    public CurrencyDto getByCode(String code) {
        CurrencyDao dao = new CurrencyDao();
        Currency currency = dao.getByCode(code).orElseThrow(() -> new NotFoundException("Currency not found: " + code));
        return CurrencyMapper.toDto(currency);
    }

    public List<CurrencyDto> getAll() {
        CurrencyDao dao = new CurrencyDao();
        List<Currency> currencies = new ArrayList<>(dao. getAll());
        List<CurrencyDto> currencyDtos = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyDtos.add(CurrencyMapper.toDto(currency));
        }
        return currencyDtos;
    }
}
