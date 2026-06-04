package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyDto;
import ru.murlov.exception.ValidationException;
import ru.murlov.service.CurrencyService;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            throw new ValidationException("Missing currency code");
        }

        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            throw new NotFoundException("Invalid path");
        }

        CurrencyService currencyService = new CurrencyService();
        String code = parts[1];
        CurrencyDto currencyDto = currencyService.getByCode(code);
        sendResponse(response, 200, currencyDto, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}