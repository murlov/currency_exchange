package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.ExchangeRequest;
import ru.murlov.dto.ExchangeResponse;
import ru.murlov.service.ExchangeService;
import ru.murlov.util.FormatUtil;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ExchangeService exchangeService = new ExchangeService();
        ObjectMapper mapper = new ObjectMapper();

        String baseCurrencyCode = FormatUtil.getRequiredNormalizedParameter(request, "from");
        String targetCurrencyCode = FormatUtil.getRequiredNormalizedParameter(request, "to");
        float amount;
        try {
            amount = Float.parseFloat(FormatUtil.getRequiredNormalizedParameter(request, "amount"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Amount must be a decimal number");
        }

        ExchangeRequest exchangeRequest = new ExchangeRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                amount);

        ExchangeResponse exchangeResponse = exchangeService.exchange(exchangeRequest);
        sendResponse(response, 200, exchangeResponse, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}