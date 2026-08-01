package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.exception.ValidationException;
import ru.murlov.service.CurrencyService;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private final static int EXPECTED_PATH_PARTS = 2;
    private final static int CODE_PART_INDEX = 1;
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        super.init();

        this.currencyService =
                (CurrencyService) getServletContext()
                        .getAttribute("currencyService");

        if (currencyService == null) {
            throw new IllegalStateException(
                    "CurrencyService is not initialized"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            throw new ValidationException("Missing currency code");
        }

        String[] parts = pathInfo.split("/");
        if (parts.length != EXPECTED_PATH_PARTS) {
            throw new NotFoundException("Invalid path");
        }

        String code = parts[CODE_PART_INDEX];
        CurrencyResponse currencyResponse = currencyService.getByCode(code);
        sendResponse(response, HttpServletResponse.SC_OK, currencyResponse, mapper);
    }
}