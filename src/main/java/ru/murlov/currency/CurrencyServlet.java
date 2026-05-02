package ru.murlov.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String pathInfo = request.getPathInfo();
        Map<String, Object> error;

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            error = Map.of(
                    "status", 400,
                    "error", "Bad request",
                    "message", "Missing currency code"
            );
            sendError(response, 400, error, mapper);
        }

        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            error = Map.of(
                    "status", 404,
                    "error", "Not found",
                    "message", "Invalid path"
            );
            sendError(response, 404, error, mapper);
        }

        CurrencyService currencyService = new CurrencyService();
        String code = parts[1];
        try {
            CurrencyDto currencyDto = currencyService.getByCode(code);
            sendJson(response, currencyDto, mapper);
        } catch (NotFoundException e) {
            error = Map.of(
                    "status", 404,
                    "error", "Not found",
                    "message", "Currency not found"
            );
            sendError(response, 404, error, mapper);
        }

    }

    private void sendError(HttpServletResponse response, int status, Map<String, Object> error, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), error);
    }

    private void sendJson(HttpServletResponse response, CurrencyDto currencyDto, ObjectMapper mapper) throws IOException {
        response.setStatus(200);
        mapper.writeValue(response.getWriter(), currencyDto);
    }
}