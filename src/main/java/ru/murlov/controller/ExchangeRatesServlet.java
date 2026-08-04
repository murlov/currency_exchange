package ru.murlov.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRateRequest;
import ru.murlov.dto.ExchangeRateResponse;
import ru.murlov.service.ExchangeRateService;
import ru.murlov.util.validator.ExchangeRateValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.math.BigDecimal;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRateResponse> exchangeRateResponses = exchangeRateService.getAll();

        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponses);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = FormatUtil
                .getRequiredNormalizedStringParameter(request, "baseCurrencyCode");
        String targetCurrencyCode = FormatUtil
                .getRequiredNormalizedStringParameter(request, "targetCurrencyCode");
        BigDecimal rate = FormatUtil.getRequiredNormalizedBigDecimalParameter(request, "rate");

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                rate
        );

        ExchangeRateValidator.validate(exchangeRateRequest);

        ExchangeRateResponse exchangeRateResponse = exchangeRateService.save(exchangeRateRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeRateResponse);
    }
}