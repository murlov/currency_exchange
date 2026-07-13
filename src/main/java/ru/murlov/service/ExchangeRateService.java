package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRateCreateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.ExchangeRateMapper;
import ru.murlov.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {

    public List<ExchangeRateResponse> getAll() {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        List<ExchangeRate> exchangeRates;
        List<ExchangeRateResponse> exchangeRateResponses = new ArrayList<>();

        exchangeRates = exchangeRateDao.getAll();

        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateResponses.add(
                    ExchangeRateMapper.toDto(exchangeRate)
            );
        }

        return exchangeRateResponses;
    }

    public ExchangeRateResponse getByCodesPair(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        ExchangeRate exchangeRate = exchangeRateDao.getByCodesPair(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                + baseCurrencyCode + " - " + targetCurrencyCode));

        return ExchangeRateMapper.toDto(exchangeRate);
    }
}
