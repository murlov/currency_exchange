package ru.murlov.service;

import ru.murlov.dao.CurrencyDao;
import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.CurrencyMapper;
import ru.murlov.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {

    public CurrencyResponse getByCode(String code) {
        CurrencyDao currencyDao = new CurrencyDao();
        Currency currency = currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency not found: " + code));
        return CurrencyMapper.toDto(currency);
    }

    public List<CurrencyResponse> getAll() {
        CurrencyDao currencyDao = new CurrencyDao();
        List<Currency> currencies = new ArrayList<>(currencyDao. getAll());
        List<CurrencyResponse> currencyResponses = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyResponses.add(CurrencyMapper.toDto(currency));
        }
        return currencyResponses;
    }

    public CurrencyResponse save(CurrencyCreateRequest currencyCreateRequest) {
        CurrencyDao currencyDao = new CurrencyDao();
        Currency currency = CurrencyMapper.toModel(currencyCreateRequest);

        Currency newCurrency = currencyDao.save(currency);
        return CurrencyMapper.toDto(newCurrency);
    }
}
