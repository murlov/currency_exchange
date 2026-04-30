package ru.murlov.currency;

import ru.murlov.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CurrencyDao {

    private final static String READ_SQL = """
                                            SELECT * FROM currencies
                                            WHERE code = ?
                                           """;
    public Optional<Currency> getByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(READ_SQL)) {
            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();
            Currency currency = null;

            if (resultSet.next()) {
                String tempSign = resultSet.getString("sign");
                if (tempSign == null || tempSign.length() != 1) {
                    throw new RuntimeException("Column 'sign' must contain exactly one character");
                }
                char sign = tempSign.charAt(0);
                currency = new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        sign);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
