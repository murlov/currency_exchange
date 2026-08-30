package ru.murlov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.dto.CurrencyCreateRequest;
import ru.murlov.dto.CurrencyResponse;
import ru.murlov.mapper.CurrencyMapper;
import ru.murlov.model.Currency;
import ru.murlov.service.CurrencyService;
import ru.murlov.util.validator.CurrencyValidator;
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
        List<CurrencyResponse> currencyResponses = new ArrayList<>();
        for (Currency currency : currencyService.getAll()) {
            currencyResponses.add(CurrencyMapper.toDto(currency));
        }
        sendResponse(response, HttpServletResponse.SC_OK, currencyResponses);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = FormatUtil.getRequiredNormalizedStringParameter(request, "code");
        String name = FormatUtil.getRequiredNormalizedStringParameter(request, "name");
        String sign = FormatUtil.getRequiredNormalizedStringParameter(request, "sign");

        CurrencyCreateRequest currencyCreateRequest = new CurrencyCreateRequest(
                code,
                name,
                sign
        );

        CurrencyValidator.validate(currencyCreateRequest);

        Currency currency = currencyService.save(currencyCreateRequest);

        sendResponse(response, HttpServletResponse.SC_CREATED, CurrencyMapper.toDto(currency));
    }
}