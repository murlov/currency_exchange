package ru.murlov.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.exceptions.NotFoundException;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String pathInfo = request.getPathInfo();
        String message;

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            message = "Missing currency code";
            sendError(response, 400, message, mapper);
        }

        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            message = "Invalid path";
            sendError(response, 404, message, mapper);
        }

        CurrencyService currencyService = new CurrencyService();
        String code = parts[1];
        try {
            CurrencyDto currencyDto = currencyService.getByCode(code);
            sendJson(response, currencyDto, mapper);
        } catch (NotFoundException e) {
            message = "Currency not found";
            sendError(response, 404, message, mapper);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Unexpected error occurred";
            sendError(response, 500, message, mapper);
        }

    }

    private void sendError(HttpServletResponse response, int status, String message, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), message);
    }

    private void sendJson(HttpServletResponse response, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(200);
        mapper.writeValue(response.getWriter(), value);
    }
}