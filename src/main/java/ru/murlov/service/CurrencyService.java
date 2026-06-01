package ru.murlov.service;

import ru.murlov.dao.CurrencyDao;
import ru.murlov.dto.CurrencyDto;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.CurrencyMapper;
import ru.murlov.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {

    public CurrencyDto getByCode(String code) {
        CurrencyDao currencyDao = new CurrencyDao();
        Currency currency = currencyDao.getByCode(code).orElseThrow(() -> new NotFoundException("Currency not found: " + code));
        return CurrencyMapper.toDto(currency);
    }

    public List<CurrencyDto> getAll() {
        CurrencyDao currencyDao = new CurrencyDao();
        List<Currency> currencies = new ArrayList<>(currencyDao. getAll());
        List<CurrencyDto> currencyDtos = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyDtos.add(CurrencyMapper.toDto(currency));
        }
        return currencyDtos;
    }

    public CurrencyDto save(CurrencyDto currencyDto) {
        CurrencyDao currencyDao = new CurrencyDao();
        Currency currency = CurrencyMapper.toModel(currencyDto);

        Currency newCurrency = currencyDao.save(currency);
        return CurrencyMapper.toDto(newCurrency);
    }
}
