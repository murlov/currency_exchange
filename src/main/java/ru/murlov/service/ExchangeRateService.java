package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.UnexpectedRowsAffectedException;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.Currency;
import ru.murlov.model.CurrencyPair;
import ru.murlov.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {

    private static final int EXPECTED_UPDATED_ROWS = 1;
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public List<ExchangeRate> getAll() {
        return exchangeRateDao.getAll();
    }

    public ExchangeRate getByCodesPair(CurrencyPair currencyPair) {
        return exchangeRateDao.getByCodesPair(currencyPair)
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                + currencyPair.baseCurrencyCode() + " - " + currencyPair.targetCurrencyCode()));
    }


    public ExchangeRate save(ExchangeRateRequest exchangeRateRequest) {
        if (!isRateValid(exchangeRateRequest.rate())) {
            throw new ValidationException("Rate must be bigger than zero");
        }

        ExchangeRate exchangeRate = createExchangeRate(exchangeRateRequest);

        return exchangeRateDao.save(exchangeRate);
    }

    public ExchangeRate update(ExchangeRateRequest exchangeRateRequest) {
        if (!isRateValid(exchangeRateRequest.rate())) {
            throw new ValidationException("Rate must be bigger than zero");
        }

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

        return newExchangeRate;
    }

    private boolean isRateValid(BigDecimal rate) {
        BigDecimal value = new BigDecimal("0");
        return rate.compareTo(value) > 0;
    }

    private ExchangeRate createExchangeRate(ExchangeRateRequest exchangeRateRequest) {

        Currency baseCurrency = currencyService.
                getByCode(exchangeRateRequest.baseCurrencyCode());
        Currency targetCurrency = currencyService
                .getByCode(exchangeRateRequest.targetCurrencyCode());

        return new ExchangeRate(
                baseCurrency,
                targetCurrency,
                exchangeRateRequest.rate()
        );
    }
}
