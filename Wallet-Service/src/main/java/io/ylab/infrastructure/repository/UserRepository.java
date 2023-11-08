package io.ylab.infrastructure.repository;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class UserRepository {
    public static  String URL = null;
    public  static String USERNAME = null;
    public static  String PASSWORD = null;

    {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("application.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        URL = properties.getProperty("db.url");
        USERNAME = properties.getProperty("db.username");
        PASSWORD = properties.getProperty("db.password");
    }

    /**
     * Добавить пользователя в БД.
     *
     * @param user передаем созданного юзера.
     */
    public void saveUser(User user) {
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement =
                    connection.prepareStatement(
                            "INSERT INTO data.users(name, login, password, balance) VALUES(?,?,?,?)"
                    );
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDouble(4, user.getBalance());
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка в методе saveUser. log: " + e.getMessage());
        }
    }

    /**
     * Получить пользователя по его логину.
     *
     * @param login логин пользователя в качестве ключа.
     * @return объект User.
     */
    public User findByLogin(String login) {

        User user = new User();
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM data.users WHERE login=?");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setBalance(resultSet.getDouble("balance"));

                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка в методе findByLogin. log: " + e.getMessage());
        }
        return user;
    }

    /**
     * Проверка существования пользователя в БД.
     *
     * @param login логин пользователя в качестве ключа.
     * @return true если присутствует.
     */
    public boolean userPresence(String login) {
        User user = findByLogin(login);
        if (user != null && login.equals(user.getLogin())) {
            return true;
        }
        return false;
    }

    /**
     * Обновление баланса у пользователя.
     *
     * @param user пользователь, которому будет присвоено обновление.
     */
    public void updateBalance(User user) {
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE data.users SET balance=? WHERE login=?");
            preparedStatement.setDouble(1, user.getBalance());
            preparedStatement.setString(2, user.getLogin());

            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка в методе updateBalance. log: " + e.getMessage());
        }
    }

    /**
     * Добавить транзакцию пользователю
     *
     * @param user            пользователь, которому будет присвоена транзакция.
     * @param transactionType тип транзакции.
     * @param transactionSum  сумма транзакции.
     */
    public void saveTransaction(User user, TransactionType transactionType, double transactionSum) {
        user = findByLogin(user.getLogin());
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO data.transactions(user_id, transaction_type, transaction_sum, date_time) VALUES (?,?,?,?)");

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, transactionType.toString());
            preparedStatement.setDouble(3, transactionSum);
            preparedStatement.setString(4, time());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка в методе addTransaction. log: " + e.getMessage());
        }
    }

    /**
     * Получить транзакцию пользователя.
     *
     * @param user пользователь чью транзакцию считываем.
     */
    public void getTransaction(User user) {
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT transaction_type, transaction_sum, date_time FROM data.transactions WHERE user_id = ?");
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String transaction = resultSet.getString("transaction_type");
                double sum = resultSet.getDouble("transaction_sum");
                String time = resultSet.getString("date_time");

                System.out.println("Транзакция: " + transaction + " сумма: " + sum + " Дата и время: " + time);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка в методе getTransaction. log: " + e.getMessage());
        }
    }

    /**
     * Добавить активность в список.
     *
     * @param user        пользователь, которому будет присвоена активность.
     * @param userActions активность.
     */
    public void saveAction(User user, UserActions userActions) {
        user = findByLogin(user.getLogin());
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO data.actions(user_id, user_actions, date_time) VALUES (?,?,?)");
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, userActions.toString());
            preparedStatement.setString(3, time());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить список активностей пользователя.
     */
    public void getAction() {
        try {
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT a.user_actions, a.date_time, u.login\n" +
                            "FROM data.actions a\n" +
                            "JOIN data.users u ON a.user_id = u.id ");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String action = resultSet.getString("user_actions");
                String time = resultSet.getString("date_time");
                String login = resultSet.getString("login");
                System.out.println("Пользователь: " + login + ", действие: " + action + ", Дата и время: " + time);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка в методе getAction. log: " + e.getMessage());
        }
    }

    /**
     * Форматирование даты и времени.
     *
     * @return время в строковом формате.
     */
    public String time() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatterDataTime = now.format(formatter);
        return formatterDataTime;
    }
}
