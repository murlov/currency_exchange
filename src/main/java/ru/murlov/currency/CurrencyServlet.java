package ru.murlov.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            sendError(response, 400, "Missing currency code", mapper);
        }

        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            sendError(response, 404, "Invalid path", mapper);
        } else if (parts.length == 0) {
            sendError(response, 400, "Missing currency code", mapper);
        }

        CurrencyService currencyService = new CurrencyService();
        String code = parts[1];
        try {
            CurrencyDto currencyDto = currencyService.getByCode(code);
            sendJson(response, currencyDto, mapper);
        } catch (NotFoundException e) {
            sendError(response, 404, "Currency not found", mapper);
        }

    }

    private void sendError(HttpServletResponse response, int status, String message, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), message);
    }

    private void sendJson(HttpServletResponse response, CurrencyDto currencyDto, ObjectMapper mapper) throws IOException {
        response.setStatus(200);
        mapper.writeValue(response.getWriter(), currencyDto);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}