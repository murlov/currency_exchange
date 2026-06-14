package ru.murlov.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.murlov.util.ConnectionManager;
import ru.murlov.util.DatabaseInitializer;

@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionManager.initConnectionPool();
            DatabaseInitializer.init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DB", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.shutdown();
    }
}
