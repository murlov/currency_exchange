package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.dto.ExchangeRateCreateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.CurrencyMapper;
import ru.murlov.mapper.ExchangeRateMapper;
import ru.murlov.model.CurrencyPair;
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

    public ExchangeRateResponse getByCodesPair(CurrencyPair currencyPair) {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        ExchangeRate exchangeRate = exchangeRateDao.getByCodesPair(currencyPair.baseCurrencyCode(), currencyPair.targetCurrencyCode())
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                + currencyPair.baseCurrencyCode() + " - " + currencyPair.targetCurrencyCode()));

        return ExchangeRateMapper.toDto(exchangeRate);
    }


    public ExchangeRateResponse save(ExchangeRateCreateRequest exchangeRateCreateRequest) {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

        ExchangeRate exchangeRate = createExchangeRate(exchangeRateCreateRequest);

        ExchangeRate newExchangeRate = exchangeRateDao.save(exchangeRate);
        return ExchangeRateMapper.toDto(newExchangeRate);
    }

    private ExchangeRate createExchangeRate(ExchangeRateCreateRequest exchangeRateCreateRequest) {
        CurrencyService currencyService = new CurrencyService();

        CurrencyResponse baseCurrencyResponse = currencyService.
                getByCode(exchangeRateCreateRequest.baseCurrencyCode());
        CurrencyResponse targetCurrencyResponse = currencyService
                .getByCode(exchangeRateCreateRequest.targetCurrencyCode());

        return ExchangeRateMapper.toModel(baseCurrencyResponse,
                targetCurrencyResponse,
                exchangeRateCreateRequest.rate());
    }
}
