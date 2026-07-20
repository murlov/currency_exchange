package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ObjectMapper mapper = new ObjectMapper();

        CurrencyPair currencyPair = parseCurrencyPair(request);

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.getByCodesPair(currencyPair);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse, mapper);
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ObjectMapper mapper = new ObjectMapper();

        CurrencyPair currencyPair = parseCurrencyPair(request);
        float rate;
        try {
            rate = Float.parseFloat(FormatUtil.getRequiredNormalizedParameter(request, "rate"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Rate must be a decimal number.");
        }

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                currencyPair.baseCurrencyCode(),
                currencyPair.targetCurrencyCode(),
                rate
        );

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.update(exchangeRateRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse, mapper);
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
            throw new ValidationException("Currency codes pair must contain exactly 6 letters");
        }

        String baseCurrencyCode = codesPair.substring(0,3);
        String targetCurrencyCode = codesPair.substring(3, 6);

        return new CurrencyPair(baseCurrencyCode, targetCurrencyCode);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}