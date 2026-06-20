package ru.murlov.dao;

import ru.murlov.exception.DatabaseException;
import ru.murlov.model.Currency;
import ru.murlov.model.ExchangeRate;
import ru.murlov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            tc.sign as target_currency_sign,
            
            er.rate
            
            FROM exchange_rates er
            JOIN currencies bc ON er.base_currency_id = bc.id
            JOIN currencies tc ON er.target_currency_id = tc.id
            """;

    private final static String GET_BY_CODES_PAIR_SQL =
            GET_ALL_SQL +
            """
            WHERE bc.code = ? AND tc.code = ?
            """;

    public List<ExchangeRate> getAll() {

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(
                        getExchangeRate(resultSet)
                );
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Optional<ExchangeRate> getByCodesPair(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(GET_BY_CODES_PAIR_SQL)) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            ResultSet resultSet = statement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (resultSet.next()) {
                exchangeRate = getExchangeRate(resultSet);
            }

            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private ExchangeRate getExchangeRate (ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getLong("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_name"),
                resultSet.getString("base_currency_sign")
        );
        Currency targetCurrency = new Currency(
                resultSet.getLong("target_currency_id"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_name"),
                resultSet.getString("target_currency_sign")
        );
        return new ExchangeRate(
                resultSet.getLong("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getFloat("rate")
        );
    }
}
