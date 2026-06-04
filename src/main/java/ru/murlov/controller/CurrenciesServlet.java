package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyDto;
import ru.murlov.exception.ValidationException;
import ru.murlov.service.CurrencyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        CurrencyService currencyService = new CurrencyService();
        List<CurrencyDto> currencyDtos = new ArrayList<>(currencyService.getAll());

        sendResponse(response, 200, currencyDtos, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String tempSign = request.getParameter("sign");

        if (code == null || code.isBlank()) {
            throw new ValidationException("Parameter 'code' is required");
        }

        if (name == null || name.isBlank()) {
            throw new ValidationException("Parameter 'name' is required");
        }

        if (tempSign == null || tempSign.isBlank()) {
            throw new ValidationException("Parameter 'sign' is required");
        } else if (tempSign.length() != 1) {
            throw new ValidationException("Parameter 'sign' must contain exactly one character");
        }
        char sign = tempSign.charAt(0);
        CurrencyDto currencyDto = new CurrencyDto(
                code,
                name,
                sign
        );

        CurrencyService currencyService = new CurrencyService();
        CurrencyDto newCurrencyDto;
        newCurrencyDto = currencyService.save(currencyDto);
        sendResponse(response, 200, newCurrencyDto, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}