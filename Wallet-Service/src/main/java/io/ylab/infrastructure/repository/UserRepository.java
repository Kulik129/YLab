package io.ylab.infrastructure.repository;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;

import java.sql.*;
import java.util.List;

public class UserRepository {

//
////    private Map<String, User> userMap = new HashMap<>();
////    private List<LogEntry> listActions = new ArrayList<>();
////    /**
////     * Для автоматической генерации ID транзакции пользователя.
////     */
////    public static int TRANSACTION_ID;
////
////    /**
////     * Для автоматической генерации ID действия пользователя.
////     */
////    public static int ACTION_ID;
//
////    /**
////     * Пользователи для тестирования.
////     */ {
////        userMap.put("admin", new User(userMap.size() + 1, "Dmitrii", "admin", "1234"));
////        userMap.put("vik", new User(userMap.size() + 1, "Viktor", "vik", "4321"));
////    }
//
//    //regionUserMap
//
//    /**
//     * Обратиться к мапе.
//     *
//     * @return мапа пользователей.
//     */
////    public Map<String, User> getUserMap() {
////        return userMap;
////    }
//

    //regionConnection
    private static final String URL = "jdbc:postgresql://localhost:5432/ylab";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345678";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //regionUser

    /**
     * Добавить пользователя в БД.
     *
     * @param user передаем созданного юзера.
     */
    public void saveUserInDataBase(User user) {
        try {
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить пользователя по его логину.
     *
     * @param login логин пользователя в качестве ключа.
     * @return объект User.
     */
    public User getUser(String login) {
        User user = new User();
        try {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        User user = getUser(login);
        if (user != null && login.equals(user.getLogin())) {
            return true;
        }
        return false;
    }

    public void updateBalance(User user) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE data.users SET balance=? WHERE login=?");
            preparedStatement.setDouble(1, user.getBalance());
            preparedStatement.setString(2,user.getLogin());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //regionTransaction

    /**
     * Добавить транзакцию пользователю
     *
     * @param user            пользователь, которому будет присвоена транзакция.
     * @param transactionType тип транзакции.
     * @param sum             сумма транзакции.
     */
    public void addTransaction(User user, TransactionType transactionType, double sum) {
//        Transaction transaction = new Transaction(++TRANSACTION_ID, transactionType, sum, LocalDateTime.now());
//        user.getTransactions().add(transaction);
    }

    /**
     * Получить транзакцию пользователя.
     *
     * @param user пользователь чью транзакцию считываем.
     */
    public void getTransaction(User user) {
        for (Transaction transaction : user.getTransactions()) {
            System.out.println(transaction);
        }
    }

    //endregion

    //regionLogEntry

    /**
     * Добавить активность в список.
     *
     * @param user        пользователь, которому будет присвоена активность.
     * @param userActions активность.
     */
    public void addLogEntry(User user, UserActions userActions) {
//        LogEntry logEntry = new LogEntry(++ACTION_ID, userActions, LocalDateTime.now(), user);
//        user.getActionLog().add(logEntry);
//        listActions.add(logEntry);
    }

    /**
     * Получить список активностей пользователя.
     *
     * @param user пользователь чьи активности будут переданы для считывания.
     */
    public void getLogEntry(User user) {
        for (LogEntry log : user.getActionLog()) {
            System.out.println(log);
        }
    }

    /**
     * Получить доступ к общему листу действий всех пользователей.
     *
     * @return лист активностей.
     */
    public List<LogEntry> getListActions() {
        return null;
    }

    //endregion
}
