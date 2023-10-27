package io.ylab.application;

import io.ylab.domain.service.UserServiceImpl;
import io.ylab.infrastructure.migrations.DatabaseMigrator;
import io.ylab.infrastructure.in.InputDataService;
import io.ylab.infrastructure.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Точка входа в приложение.
 */
public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("application.properties");
            properties.load(inputStream);

            String URL = properties.getProperty("db.url");
            String USER_NAME = properties.getProperty("db.username");
            String PASSWORD = properties.getProperty("db.password");

            DatabaseMigrator.runDatabaseMigrations(URL, USER_NAME, PASSWORD);
            UserRepository userRepository = new UserRepository(URL, USER_NAME, PASSWORD);
            UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository);
            InputDataService inputDataService = new InputDataService(userServiceImpl);

            inputDataService.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}