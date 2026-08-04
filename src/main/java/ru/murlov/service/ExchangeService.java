package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.dto.ExchangeResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.CurrencyPair;
import ru.murlov.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public ExchangeResponse exchange(ExchangeRequest exchangeRequest) {
        BigDecimal convertedAmount;
        BigDecimal newRate;

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
            convertedAmount = exchangeRequest.amount().multiply(newRate);
        } else if (targetCurrencyToBaseCurrency.isPresent()) {
            newRate = new BigDecimal("1").divide(targetCurrencyToBaseCurrency.get().getRate(), 2, RoundingMode.HALF_UP);
            convertedAmount = exchangeRequest.amount().multiply(newRate);
        } else if (USDToBaseCurrency.isPresent() && USDToTargetCurrency.isPresent()) {
            newRate = USDToTargetCurrency.get().getRate().divide(USDToBaseCurrency.get().getRate(), 2, RoundingMode.HALF_UP);
            convertedAmount = exchangeRequest.amount().multiply(newRate);
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

    private boolean amountIsValid(BigDecimal amount) {
        BigDecimal value = new BigDecimal("0");
        return amount.compareTo(value) == 0 || amount.compareTo(value) > 0;
    }
}
