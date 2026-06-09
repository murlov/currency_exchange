package ru.murlov.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(SQLException message) {
        super(message);
    }
}
