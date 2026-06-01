package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyDto;
import ru.murlov.service.CurrencyService;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String pathInfo = request.getPathInfo();
        Map<String, Object> error;

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            error = Map.of(
                    "message", "Missing currency code"
            );
            sendResponse(response, 400, error, mapper);
        }

        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            error = Map.of(
                    "message", "Invalid path"
            );
            sendResponse(response, 404, error, mapper);
        }

        CurrencyService currencyService = new CurrencyService();
        String code = parts[1];
        try {
            CurrencyDto currencyDto = currencyService.getByCode(code);
            sendResponse(response, 200, currencyDto, mapper);
        } catch (NotFoundException e) {
            error = Map.of(
                    "message", "Currency not found"
            );
            sendResponse(response, 404, error, mapper);
        } catch (Exception e) {
            e.printStackTrace();
            error = Map.of(
                    "message", "Unexpected error occurred"
            );
            sendResponse(response, 500, error, mapper);
        }

    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}