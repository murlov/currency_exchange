package ru.murlov.service;

import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.dto.ExchangeRateDto;
import ru.murlov.exception.NotFoundException;
import ru.murlov.mapper.ExchangeRateMapper;
import ru.murlov.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {

    public List<ExchangeRateDto> getAll() {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        List<ExchangeRate> exchangeRates;
        List<ExchangeRateDto> exchangeRateDtos = new ArrayList<>();

        exchangeRates = exchangeRateDao.getAll();

        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateDtos.add(
                    ExchangeRateMapper.toDto(exchangeRate)
            );
        }

        return exchangeRateDtos;
    }

    public ExchangeRateDto getByCodesPair(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        ExchangeRate exchangeRate = exchangeRateDao.getByCodesPair(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("ExchangeRate not found: "
                + baseCurrencyCode + " - " + targetCurrencyCode));

        return ExchangeRateMapper.toDto(exchangeRate);
    }
}
