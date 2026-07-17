package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.exception.ValidationException;
import ru.murlov.service.ExchangeRateService;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ObjectMapper mapper = new ObjectMapper();
        List<ExchangeRateResponse> exchangeRateResponses = exchangeRateService.getAll();

        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponses, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ObjectMapper mapper = new ObjectMapper();

        String baseCurrencyCode = FormatUtil.getRequiredNormalizedParameter(request, "baseCurrencyCode");
        String targetCurrencyCode = FormatUtil.getRequiredNormalizedParameter(request, "targetCurrencyCode");

        float rate;
        try {
            rate = Float.parseFloat(FormatUtil.getRequiredNormalizedParameter(request, "rate"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Rate must be a decimal number.");
        }

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                rate
        );

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.save(exchangeRateRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}