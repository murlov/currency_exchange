package ru.murlov.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {}

    static {
        init();
    }

    private static void init() {
        try (Connection connection = ConnectionManager.get();
             Statement statement = connection.createStatement()) {

            InputStream schemaInputStream = DatabaseInitializer.class.
                    getClassLoader().getResourceAsStream("db/schema.sql");

            String schema = null;
            if (schemaInputStream != null) {
                schema = new String(schemaInputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            InputStream seedInputStream = DatabaseInitializer.class.
                    getClassLoader().getResourceAsStream("db/seed.sql");

            String seed = null;
            if (seedInputStream != null) {
                seed = new String(seedInputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            String[] queries = (schema + seed).split(";");
            for (String query : queries) {
                if (!query.strip().isBlank()) {
                    statement.executeUpdate(query);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void initialize() {}
}
