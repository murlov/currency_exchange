package ru.murlov.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {

    private static final int DEFAULT_POOL_SIZE = 10;
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final BlockingQueue<Connection> pool;
    private static final BlockingQueue<Connection> sourceConnections;
    private static final int poolSize;

    static {
        String poolSizeValue = PropertiesUtil.get(POOL_SIZE_KEY);
        poolSize = poolSizeValue == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSizeValue);
        pool = new ArrayBlockingQueue<>(poolSize);
        sourceConnections = new ArrayBlockingQueue<>(poolSize);
    }

    public static void initConnectionPool() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < poolSize; i++) {
            Connection connection = open();
            sourceConnections.add(connection);
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("close")) {
                            pool.add((Connection) proxy);
                            return null;
                        }
                        try {
                            return method.invoke(connection, args);
                        } catch (Exception e) {
                            throw e.getCause();
                        }
                    }
            );
            pool.add(proxyConnection);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
        String path = PropertiesUtil.get("db.path");
        try {
            return DriverManager
                    .getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdown() {
        for (Connection connection : sourceConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
