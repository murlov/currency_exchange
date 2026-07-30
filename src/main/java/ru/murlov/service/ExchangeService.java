package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.dto.ExchangeResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.CurrencyPair;
import ru.murlov.model.ExchangeRate;

import java.util.Optional;

public class ExchangeService {

    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public ExchangeResponse exchange(ExchangeRequest exchangeRequest) {
        float convertedAmount;
        float newRate;

        if (!amountIsValid(exchangeRequest.amount())) {
            throw new ValidationException("Amount must be not less than zero");
        }

        Optional<ExchangeRate> baseCurrencyToTargetCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.baseCurrencyCode(),
                        exchangeRequest.targetCurrencyCode()
                )
        );

        Optional<ExchangeRate> targetCurrencyToBaseCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.targetCurrencyCode(),
                        exchangeRequest.baseCurrencyCode()
                )
        );

        Optional<ExchangeRate> USDToBaseCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        "USD",
                        exchangeRequest.baseCurrencyCode()
                )
        );

        Optional<ExchangeRate> USDToTargetCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        "USD",
                        exchangeRequest.targetCurrencyCode()
                )
        );

        if (baseCurrencyToTargetCurrency.isPresent()) {
            newRate = baseCurrencyToTargetCurrency.get().getRate();
            convertedAmount = exchangeRequest.amount() * newRate;
        } else if (targetCurrencyToBaseCurrency.isPresent()) {
            newRate = 1 / targetCurrencyToBaseCurrency.get().getRate();
            convertedAmount = exchangeRequest.amount() * newRate;
        } else if (USDToBaseCurrency.isPresent() && USDToTargetCurrency.isPresent()) {
            newRate = USDToTargetCurrency.get().getRate() / USDToBaseCurrency.get().getRate();
            convertedAmount = exchangeRequest.amount() * newRate;
        } else {
            throw new NotFoundException("Unable to perform exchange: no suitable exchange rate found");
        }


        return new ExchangeResponse(
                currencyService.getByCode(exchangeRequest.baseCurrencyCode()),
                currencyService.getByCode(exchangeRequest.targetCurrencyCode()),
                newRate,
                exchangeRequest.amount(),
                convertedAmount
        );
    }

    private boolean amountIsValid(float amount) {
        return amount >= 0;
    }
}
