package ru.murlov.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.dto.ExchangeResponse;
import ru.murlov.service.ExchangeService;
import ru.murlov.util.validator.ExchangeValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {

    private ExchangeService exchangeService;
    private static final int AMOUNT_DECIMAL_PRECISION = 2;

    @Override
    public void init() throws ServletException {
        super.init();

        this.exchangeService =
                (ExchangeService) getServletContext()
                        .getAttribute("exchangeService");

        if (exchangeService == null) {
            throw new IllegalStateException(
                    "ExchangeService is not initialized"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "from");
        String targetCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "to");
        BigDecimal amount = FormatUtil.getRequiredNormalizedBigDecimalParameter(
                request,
                "amount",
                AMOUNT_DECIMAL_PRECISION
        );


        ExchangeRequest exchangeRequest = new ExchangeRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                amount);

        ExchangeValidator.validate(exchangeRequest);

        ExchangeResponse exchangeResponse = exchangeService.exchange(exchangeRequest);
        sendResponse(response, HttpServletResponse.SC_OK, exchangeResponse);
    }
}