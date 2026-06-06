package ru.murlov.dao;

import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
import ru.murlov.model.Currency;
import ru.murlov.exception.DuplicateCurrencyCodeException;
import ru.murlov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private final static String GET_BY_CODE_SQL = """
                                            SELECT * FROM currencies
                                            WHERE code = ?
                                           """;

    private final static String GET_ALL_SQL = """
            SELECT id, code, full_name, sign
            FROM currencies
            """;

    private final static String SAVE_SQL = """
            INSERT INTO currencies
            (code, full_name, sign)
            VALUES (?, ?, ?)
            """;

    public Optional<Currency> getByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(GET_BY_CODE_SQL)) {
            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                currency = new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        resultSet.getString("sign"));
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> getAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<Currency> currencies = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currencies.add(
                        new Currency(
                                resultSet.getLong("id"),
                                resultSet.getString("code"),
                                resultSet.getString("full_name"),
                                resultSet.getString("sign")
                        )
                );
            }

            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency save(Currency currency) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, String.valueOf(currency.getSign()));

            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                if (isDuplicateCode(e)) {
                    throw new DuplicateCurrencyCodeException(
                            "Currency code already exists"
                    );
                }
                throw new RuntimeException(e);
            }
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                currency.setId(keys.getLong(1));
            }

            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDuplicateCode(SQLException e) {
        return (e instanceof SQLiteException sqliteE && sqliteE.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE);
    }
}
