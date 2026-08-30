package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.CurrencyPair;
import ru.murlov.model.Exchange;
import ru.murlov.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRateDao exchangeRateDao;
    private static final int RATE_DECIMAL_PRECISION = 6;
    private static final int AMOUNT_DECIMAL_PRECISION = 2;

    public ExchangeService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public Exchange exchange(ExchangeRequest exchangeRequest) {
        BigDecimal convertedAmount;

        if (!amountIsValid(exchangeRequest.amount())) {
            throw new ValidationException("Amount must be not less than zero");
        }

        ExchangeRate exchangeRate = getExchangeRate(exchangeRequest);

        convertedAmount = exchangeRequest.amount().multiply(exchangeRate.getRate());

        return new Exchange(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                exchangeRequest.amount(),
                convertedAmount.setScale(AMOUNT_DECIMAL_PRECISION, RoundingMode.HALF_UP)
        );
    }

    private ExchangeRate getExchangeRate(ExchangeRequest exchangeRequest) {
        return getDirect(exchangeRequest)
                .or(() -> getReverse(exchangeRequest))
                .or(() -> getCross(exchangeRequest))
                .orElseThrow(() -> new NotFoundException(
                        "Unable to perform exchange: no suitable exchange rate found"
                ));
    }

    private Optional<ExchangeRate> getDirect(ExchangeRequest exchangeRequest) {

        return exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.baseCurrencyCode(),
                        exchangeRequest.targetCurrencyCode()
                )
        );
    }

    private Optional<ExchangeRate> getReverse(ExchangeRequest exchangeRequest) {
        Optional<ExchangeRate> targetCurrencyToBaseCurrency = exchangeRateDao.getByCodesPair(
                new CurrencyPair(
                        exchangeRequest.targetCurrencyCode(),
                        exchangeRequest.baseCurrencyCode()
                )
        );


        return targetCurrencyToBaseCurrency.map(exchangeRate -> new ExchangeRate(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                new BigDecimal("1").divide(
                        exchangeRate.getRate(),
                        RATE_DECIMAL_PRECISION,
                        RoundingMode.HALF_UP
                )
        ));
    }

    private Optional<ExchangeRate> getCross(ExchangeRequest exchangeRequest) {
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
            return Optional.of(new ExchangeRate(
                    USDToBaseCurrency.get().getBaseCurrency(),
                    USDToTargetCurrency.get().getTargetCurrency(),
                    USDToTargetCurrencyRate.divide(
                            USDToBaseCurrencyRate,
                            RATE_DECIMAL_PRECISION,
                            RoundingMode.HALF_UP
                    )
            ));
        } else {
            return Optional.empty();
        }
    }

    private boolean amountIsValid(BigDecimal amount) {
        BigDecimal value = new BigDecimal("0");
        return amount.compareTo(value) == 0 || amount.compareTo(value) > 0;
    }
}
