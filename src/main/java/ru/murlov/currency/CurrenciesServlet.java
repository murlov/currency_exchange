package ru.murlov.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        CurrencyService currencyService = new CurrencyService();
        List<CurrencyDto> currencyDtos = new ArrayList<>(currencyService.getAll());

        sendJson(response, currencyDtos, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> error;

        String tempSign = request.getParameter("sign");
        if (tempSign == null || tempSign.length() != 1) {
            error = Map.of(
                    "message", "Parameter 'sign' must contain exactly one character"
            );
            sendError(response, 400, error, mapper);
            return;
        }
        char sign = tempSign.charAt(0);
        CurrencyDto currencyDto = new CurrencyDto(
                request.getParameter("code"),
                request.getParameter("name"),
                sign
        );

        CurrencyService currencyService = new CurrencyService();
        CurrencyDto newCurrencyDto;
        try {
            newCurrencyDto = currencyService.save(currencyDto);
        } catch (Exception e) {
            error = Map.of(
                    "message", "Unexpected error occurred"
            );
            sendError(response, 500, error, mapper);
            return;
        }
        sendJson(response, newCurrencyDto, mapper);
    }

    private void sendJson(HttpServletResponse response, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(200);
        mapper.writeValue(response.getWriter(), value);
    }

    private void sendError(HttpServletResponse response, int status, Map<String, Object> message, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), message);
    }
}