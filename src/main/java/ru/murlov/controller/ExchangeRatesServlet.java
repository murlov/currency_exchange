package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.service.ExchangeRateService;
import ru.murlov.util.validator.ExchangeRateValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        List<ExchangeRateResponse> exchangeRateResponses = exchangeRateService.getAll();

        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponses, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        String baseCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "baseCurrencyCode");
        String targetCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "targetCurrencyCode");
        float rate = FormatUtil.getRequiredNormalizedFloatParameter(request, "rate");

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                rate
        );

        ExchangeRateValidator.validate(exchangeRateRequest);

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.save(exchangeRateRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}