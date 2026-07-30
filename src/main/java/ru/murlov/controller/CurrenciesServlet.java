package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.service.CurrencyService;
import ru.murlov.util.CurrencyValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

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

        List<CurrencyResponse> currencyResponses = new ArrayList<>(currencyService.getAll());

        sendResponse(response, HttpServletResponse.SC_OK, currencyResponses, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String code = FormatUtil.getRequiredNormalizedStringParameter(request, "code");
        String name = FormatUtil.getRequiredNormalizedStringParameter(request, "name");
        String sign = FormatUtil.getRequiredNormalizedStringParameter(request, "sign");

        CurrencyCreateRequest currencyCreateRequest = new CurrencyCreateRequest(
                code,
                name,
                sign
        );

        CurrencyValidator.validate(currencyCreateRequest);

        CurrencyResponse currencyResponse;
        currencyResponse = currencyService.save(currencyCreateRequest);

        sendResponse(response, HttpServletResponse.SC_CREATED, currencyResponse, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}