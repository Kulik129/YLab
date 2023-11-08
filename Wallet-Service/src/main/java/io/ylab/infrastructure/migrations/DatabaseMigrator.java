package io.ylab.infrastructure.migrations;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseMigrator {
    /**
     * Запуск миграций
     *
     * @param jdbcUrl  URL базы данных
     * @param username Имя пользователя
     * @param password Пароль
     */
    public static void runDatabaseMigrations(String jdbcUrl, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            liquibase.database.Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/chenchelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
