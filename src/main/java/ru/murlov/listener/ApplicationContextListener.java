package ru.murlov.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.murlov.dao.CurrencyDao;
import ru.murlov.dao.ExchangeRateDao;
import ru.murlov.service.CurrencyService;
import ru.murlov.service.ExchangeRateService;
import ru.murlov.service.ExchangeService;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrencyDao currencyDao = new CurrencyDao();
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyService);
        ExchangeService exchangeService = new ExchangeService(exchangeRateDao, currencyService);

        ServletContext context = sce.getServletContext();

        context.setAttribute("currencyService", currencyService);
        context.setAttribute("exchangeRateService", exchangeRateService);
        context.setAttribute("exchangeService", exchangeService);
    }
}
