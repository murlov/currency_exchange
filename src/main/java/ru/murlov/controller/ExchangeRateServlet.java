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

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

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
        float rate = FormatUtil.getRequiredNormalizedFloatParameter(request, "rate");

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
            throw new ValidationException("Missing currency codes pair");
        }

        String[] parts = pathInfo.split("/");
        if (parts.length != 2) {
            throw new NotFoundException("Invalid path");
        }

        String codesPair = parts[1];
        if (!codesPair.matches("[A-Z]{6}")) {
            throw new ValidationException("Currency codes pair must contain exactly 6 uppercase letters");
        }

        String baseCurrencyCode = codesPair.substring(0,3);
        String targetCurrencyCode = codesPair.substring(3, 6);

        return new CurrencyPair(baseCurrencyCode, targetCurrencyCode);
    }
}