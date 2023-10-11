package io.ylab.infrastructure.repository;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private Map<String, User> userMap = new HashMap<>();
    private List<LogEntry> listActions = new ArrayList<>();
    /**
     * Для автоматической генерации ID транзакции пользователя.
     */
    public static int TRANSACTION_ID;

    /**
     * Для автоматической генерации ID действия пользователя.
     */
    public static int ACTION_ID;

    /**
     * Пользователи для тестирования.
     */ {
        userMap.put("admin", new User(userMap.size() + 1, "Dmitrii", "admin", "1234"));
        userMap.put("vik", new User(userMap.size() + 1, "Viktor", "vik", "4321"));
    }

    //regionUserMap

    /**
     * Обратиться к мапе.
     *
     * @return мапа пользователей.
     */
    public Map<String, User> getUserMap() {
        return userMap;
    }

    /**
     * Добавить пользователя в мапу.
     *
     * @param user передаем созданного юзера.
     */
    public void addUserInMap(User user) {
        userMap.put(user.getLogin(), user);
    }

    /**
     * Получить пользователя по его ключу.
     *
     * @param login логин пользователя в качестве ключа.
     * @return объект User.
     */
    public User getUser(String login) {
        return userMap.get(login);
    }

    /**
     * Проверка существования пользователя в мапе.
     *
     * @param login логин пользователя в качестве ключа.
     * @return true если присутствует.
     */
    public boolean userPresence(String login) {
        return userMap.containsKey(login);
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
        Transaction transaction = new Transaction(++TRANSACTION_ID, transactionType, sum, LocalDateTime.now());
        user.getTransactions().add(transaction);
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
        LogEntry logEntry = new LogEntry(++ACTION_ID, userActions, LocalDateTime.now(), user);
        user.getActionLog().add(logEntry);
        listActions.add(logEntry);
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
        return listActions;
    }

    //endregion
}
