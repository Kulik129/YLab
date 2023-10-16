package io.ylab.application;

import io.ylab.domain.service.UserService;
import io.ylab.infrastructure.in.InputDataService;
import io.ylab.infrastructure.repository.UserRepository;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Точка входа в приложение.
 */

public class Main {
    public static void main(String[] args) {
        // Запускаем миграции
        runDatabaseMigrations();
        // Создаем репозиторий пользователей
        UserRepository userRepository = new UserRepository();
        // Создаем сервис пользователей, внедряя в него репозиторий
        UserService userService = new UserService(userRepository);
        // Создаем сервис ввода данных, внедряя в него сервис пользователей
        InputDataService inputDataService = new InputDataService(userService);

        inputDataService.start();
    }

    /**
     * Запуск миграций
     */
    private static void runDatabaseMigrations() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/wallet",
                    "root",
                    "12345678"
            );
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/chenchelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}