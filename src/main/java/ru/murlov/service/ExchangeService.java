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
    private static final int NUMBER_OF_DECIMALS = 2;

    public ExchangeService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public ExchangeResponse exchange(ExchangeRequest exchangeRequest) {
        BigDecimal convertedAmount;

        if (!amountIsValid(exchangeRequest.amount())) {
            throw new ValidationException("Amount must be not less than zero");
        }

        BigDecimal rate = getRate(exchangeRequest);

        convertedAmount = exchangeRequest.amount().multiply(rate);

        return new ExchangeResponse(
                currencyService.getByCode(exchangeRequest.baseCurrencyCode()),
                currencyService.getByCode(exchangeRequest.targetCurrencyCode()),
                rate,
                exchangeRequest.amount(),
                convertedAmount.setScale(NUMBER_OF_DECIMALS, RoundingMode.HALF_UP)
        );
    }

    private BigDecimal getRate(ExchangeRequest exchangeRequest) {
        return getDirect(exchangeRequest)
                .or(() -> getReverse(exchangeRequest))
                .or(() -> getCross(exchangeRequest))
                .orElseThrow(() -> new NotFoundException(
                        "Unable to perform exchange: no suitable exchange rate found"
                ));
    }

    private Optional<BigDecimal> getDirect(ExchangeRequest exchangeRequest) {
        Optional<ExchangeRate> baseCurrencyToTargetCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.baseCurrencyCode(),
                        exchangeRequest.targetCurrencyCode()
                )
        );

        return baseCurrencyToTargetCurrency.map(ExchangeRate::getRate);
    }

    private Optional<BigDecimal> getReverse(ExchangeRequest exchangeRequest) {
        Optional<ExchangeRate> targetCurrencyToBaseCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.targetCurrencyCode(),
                        exchangeRequest.baseCurrencyCode()
                )
        );

        return targetCurrencyToBaseCurrency.map(
                exchangeRate -> new BigDecimal("1").divide(
                        exchangeRate.getRate(),
                        NUMBER_OF_DECIMALS,
                        RoundingMode.HALF_UP
                )
        );
    }

    private Optional<BigDecimal> getCross(ExchangeRequest exchangeRequest) {
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

        if (USDToBaseCurrency.isPresent() && USDToTargetCurrency.isPresent()) {
            BigDecimal USDToTargetCurrencyRate = USDToTargetCurrency.get().getRate();
            BigDecimal USDToBaseCurrencyRate = USDToBaseCurrency.get().getRate();
            return Optional.of(
                    USDToTargetCurrencyRate.divide(
                            USDToBaseCurrencyRate,
                            NUMBER_OF_DECIMALS,
                            RoundingMode.HALF_UP
                    )
            );
        } else {
            return Optional.empty();
        }
    }

    private boolean amountIsValid(BigDecimal amount) {
        BigDecimal value = new BigDecimal("0");
        return amount.compareTo(value) == 0 || amount.compareTo(value) > 0;
    }
}
