package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyDto;
import ru.murlov.service.CurrencyService;
import ru.murlov.util.CurrencyValidator;
import ru.murlov.util.FormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        CurrencyService currencyService = new CurrencyService();
        List<CurrencyDto> currencyDtos = new ArrayList<>(currencyService.getAll());

        sendResponse(response, HttpServletResponse.SC_OK, currencyDtos, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String code = FormatUtil.getRequiredNormalizedParameter(request, "code");
        String name = FormatUtil.getRequiredNormalizedParameter(request, "name");
        String sign = FormatUtil.getRequiredNormalizedParameter(request, "sign");

        CurrencyDto currencyDto = new CurrencyDto(
                code,
                name,
                sign
        );

        CurrencyValidator.validate(currencyDto);

        CurrencyService currencyService = new CurrencyService();
        CurrencyDto newCurrencyDto;
        newCurrencyDto = currencyService.save(currencyDto);

        sendResponse(response, HttpServletResponse.SC_CREATED, newCurrencyDto, mapper);
    }

    private void sendResponse(HttpServletResponse response, int status, Object value, ObjectMapper mapper) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), value);
    }
}