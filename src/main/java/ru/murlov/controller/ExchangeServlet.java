package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.dto.ExchangeResponse;
import ru.murlov.service.ExchangeService;
import ru.murlov.util.ExchangeValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeService exchangeService = new ExchangeService();
        ObjectMapper mapper = new ObjectMapper();

        String baseCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "from");
        String targetCurrencyCode = FormatUtil.getRequiredNormalizedStringParameter(request, "to");
        float amount = FormatUtil.getRequiredNormalizedFloatParameter(request, "amount");


        ExchangeRequest exchangeRequest = new ExchangeRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                amount);

        ExchangeValidator.validate(exchangeRequest);

        ExchangeResponse exchangeResponse = exchangeService.exchange(exchangeRequest);
        sendResponse(response, 200, exchangeResponse, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}