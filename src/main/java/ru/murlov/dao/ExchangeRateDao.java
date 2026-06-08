package ru.murlov.dao;

import ru.murlov.model.Currency;
import ru.murlov.model.ExchangeRate;
import ru.murlov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao {

    private final static String GET_ALL_SQL = """
            SELECT
            er.id,
            
            bc.id as base_currency_id,
            bc.code as base_currency_code,
            bc.full_name as base_currency_name,
            bc.sign as base_currency_sign,
            
            tc.id as target_currency_id,
            tc.code as target_currency_code,
            tc.full_name as target_currency_name,
            tc.sign as base_currency_sign,
            
            er.rate
            
            FROM exchange_rates er
            JOIN currencies bc ON er.base_currency_id = bc.id
            JOIN currencies tc ON er.target_currency_id = tc.id
            """;

    public List<ExchangeRate> getAll() {

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            Currency baseCurrency;
            Currency targetCurrency;

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                baseCurrency = new Currency(
                        resultSet.getLong("base_currency_id"),
                        resultSet.getString("base_currency_code"),
                        resultSet.getString("base_currency_name"),
                        resultSet.getString("base_currency_sign")
                );
                targetCurrency = new Currency(
                        resultSet.getLong("target_currency_id"),
                        resultSet.getString("target_currency_code"),
                        resultSet.getString("target_currency_name"),
                        resultSet.getString("target_currency_sign")
                );
                exchangeRates.add(
                        new ExchangeRate(
                                resultSet.getLong("id"),
                                baseCurrency,
                                targetCurrency,
                                resultSet.getFloat("rate")
                        )
                );
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
