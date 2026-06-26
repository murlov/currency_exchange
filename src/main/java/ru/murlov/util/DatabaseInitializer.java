package ru.murlov.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

            String schemaPath = PropertiesUtil.get("schema.path");
            String schema = Files.readString(Path.of(schemaPath));

            String seedPath = PropertiesUtil.get("seed.path");
            String seed = Files.readString(Path.of(seedPath));

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
