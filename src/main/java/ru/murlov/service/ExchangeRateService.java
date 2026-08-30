package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.UnexpectedRowsAffectedException;
import ru.murlov.exception.ValidationException;
import ru.murlov.mapper.ExchangeRateMapper;
import ru.murlov.model.CurrencyPair;
import ru.murlov.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {

    private static final int EXPECTED_UPDATED_ROWS = 1;
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public List<ExchangeRateResponse> getAll() {
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
        ExchangeRate exchangeRate = exchangeRateDao.getByCodesPair(currencyPair)
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                + currencyPair.baseCurrencyCode() + " - " + currencyPair.targetCurrencyCode()));

        return ExchangeRateMapper.toDto(exchangeRate);
    }


    public ExchangeRateResponse save(ExchangeRateRequest exchangeRateRequest) {
        if (!isRateValid(exchangeRateRequest.rate())) {
            throw new ValidationException("Rate must be bigger than zero");
        }

        ExchangeRate exchangeRate = createExchangeRate(exchangeRateRequest);

        ExchangeRate newExchangeRate = exchangeRateDao.save(exchangeRate)
                .orElseThrow(() -> new NotFoundException(
                        "Failed to save exchange rate"
                ));
        return ExchangeRateMapper.toDto(newExchangeRate);
    }

    public ExchangeRateResponse update(ExchangeRateRequest exchangeRateRequest) {
        if (!isRateValid(exchangeRateRequest.rate())) {
            throw new ValidationException("Rate must be bigger than zero");
        }

//        ExchangeRate exchangeRate = createExchangeRate(exchangeRateRequest);

        CurrencyPair currencyPair = new CurrencyPair(
                exchangeRateRequest.baseCurrencyCode(),
                exchangeRateRequest.targetCurrencyCode()
        );

        ExchangeRate currentExchangeRate = exchangeRateDao.getByCodesPair(currencyPair)
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                        + currencyPair.baseCurrencyCode() + " - " + currencyPair.targetCurrencyCode()));

        ExchangeRate newExchangeRate = new ExchangeRate(
                currentExchangeRate.getId(),
                currentExchangeRate.getBaseCurrency(),
                currentExchangeRate.getTargetCurrency(),
                exchangeRateRequest.rate()
        );

        if (exchangeRateDao.update(newExchangeRate) != EXPECTED_UPDATED_ROWS) {
            throw new UnexpectedRowsAffectedException(
                    "ExchangeRate updating failed"
            );
        }

        return ExchangeRateMapper.toDto(newExchangeRate);
    }

    private boolean isRateValid(BigDecimal rate) {
        BigDecimal value = new BigDecimal("0");
        return rate.compareTo(value) > 0;
    }

    private ExchangeRate createExchangeRate(ExchangeRateRequest exchangeRateRequest) {

        CurrencyResponse baseCurrencyResponse = currencyService.
                getByCode(exchangeRateRequest.baseCurrencyCode());
        CurrencyResponse targetCurrencyResponse = currencyService
                .getByCode(exchangeRateRequest.targetCurrencyCode());

        return ExchangeRateMapper.toModel(baseCurrencyResponse,
                targetCurrencyResponse,
                exchangeRateRequest.rate());
    }
}
