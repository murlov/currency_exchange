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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        CurrencyService currencyService = new CurrencyService();
        List<CurrencyDto> currencyDtos = new ArrayList<>(currencyService.getAll());

        sendJson(response, currencyDtos, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> error;

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String tempSign = request.getParameter("sign");

        if (code == null || code.isBlank()) {
            error = Map.of(
                    "message", "Parameter 'code' is required"
            );
            sendError(response, 400, error, mapper);
            return;
        }

        if (name == null || name.isBlank()) {
            error = Map.of(
                    "message", "Parameter 'name' is required"
            );
            sendError(response, 400, error, mapper);
            return;
        }

        if (tempSign == null || tempSign.isBlank()) {
            error = Map.of(
                    "message", "Parameter 'sign' is required"
            );
            sendError(response, 400, error, mapper);
            return;
        } else if (tempSign.length() != 1) {
            error = Map.of(
                    "message", "Parameter 'sign' must contain exactly one character"
            );
            sendError(response, 400, error, mapper);
            return;
        }
        char sign = tempSign.charAt(0);
        CurrencyDto currencyDto = new CurrencyDto(
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