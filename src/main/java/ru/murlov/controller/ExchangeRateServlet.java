package ru.murlov.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.ValidationException;
import ru.murlov.model.CurrencyPair;
import ru.murlov.service.ExchangeRateService;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    private static final int EXPECTED_PATH_PARTS = 2;
    private static final int CODES_PAIR_PART_INDEX = 1;
    private static final int CODES_PAIR_LENGTH = 6;
    private static final int BASE_CODE_START_INDEX = 0;
    private static final int BASE_CODE_END_INDEX = 3;
    private static final int TARGET_CODE_START_INDEX = 3;
    private static final int TARGET_CODE_END_INDEX = 6;
    private static final int RATE_NUMBER_OF_DECIMALS = 6;
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        super.init();

        this.exchangeRateService =
                (ExchangeRateService) getServletContext()
                        .getAttribute("exchangeRateService");

        if (exchangeRateService == null) {
            throw new IllegalStateException(
                    "ExchangeRateService is not initialized"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CurrencyPair currencyPair = parseCurrencyPair(request);

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.getByCodesPair(currencyPair);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse);
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CurrencyPair currencyPair = parseCurrencyPair(request);
        BigDecimal rate = FormatUtil.getRequiredNormalizedBigDecimalParameter(
                request,
                "rate",
                RATE_NUMBER_OF_DECIMALS
        );

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                currencyPair.baseCurrencyCode(),
                currencyPair.targetCurrencyCode(),
                rate
        );

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.update(exchangeRateRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse);
    }

    private CurrencyPair parseCurrencyPair(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();


        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            throw new ValidationException(
                    "Missing currency codes pair"
            );
        }

        String[] parts = pathInfo.split("/");
        if (parts.length != EXPECTED_PATH_PARTS) {
            throw new NotFoundException(
                    "Invalid path"
            );
        }

        String codesPair = parts[CODES_PAIR_PART_INDEX];
        if (!codesPair.matches("[A-Z]{" + CODES_PAIR_LENGTH + "}")) {
            throw new ValidationException(
                    "Currency codes pair must contain exactly 6 uppercase letters"
            );
        }

        String baseCurrencyCode = codesPair.substring(
                BASE_CODE_START_INDEX,
                BASE_CODE_END_INDEX
        );
        String targetCurrencyCode = codesPair.substring(
                TARGET_CODE_START_INDEX,
                TARGET_CODE_END_INDEX
        );

        return new CurrencyPair(baseCurrencyCode, targetCurrencyCode);
    }
}