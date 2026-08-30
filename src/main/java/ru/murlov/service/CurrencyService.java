package ru.murlov.service;

import ru.murlov.dao.CurrencyDao;
import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.CurrencyMapper;
import ru.murlov.model.Currency;

import java.util.List;

public class CurrencyService {

    private final CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public Currency getByCode(String code) {
        return currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException(
                        "Currency not found: " + code
                ));
    }

    public List<Currency> getAll() {
        return currencyDao. getAll();
    }

    public Currency save(CurrencyCreateRequest currencyCreateRequest) {
        Currency currency = CurrencyMapper.toModel(currencyCreateRequest);

        return currencyDao.save(currency);
    }
}
