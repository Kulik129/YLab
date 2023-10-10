package io.ylab.domain.service;

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

/**
 * Основная бизнес логика.
 * Сервис обработки основных методов.
 */

public class UserService implements ServiceUser {
    /**
     * Для автоматической генерации ID пользователя.
     */
    private static int CREATE_USER_ID;
    /**
     *  Для автоматической генерации ID действия пользователя.
     */
    private static int GENERATE_ID_ACTION;
    /**
     * Для автоматической генерации ID действия.
     */
    private static int GENERATE_ID_TRANSACTION;
    /**
     * Пользователи.
     */
    public Map<String, User> userMap = new HashMap<>();
    /**
     * Действия.
     */
    public List<LogEntry> listActions = new ArrayList<>();
    /**
     * Транзакции.
     */
    public List<Transaction> transactions = new ArrayList<>();

    /**
     * Пользователи для тестирования.
     */

    {
        userMap.put("admin", new User(++CREATE_USER_ID, "Dmitrii", "admin", "1234"));
        userMap.put("vik", new User(++CREATE_USER_ID, "Viktor", "vik", "4321"));
    }

    /**
     * Регистрация пользователя
     * @param name имя пользователя.
     * @param login логин пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public void registrationUser(String name, String login, String password) {
        User user = new User(++CREATE_USER_ID, name, login, password);
        userMap.put(login, user);
        auditOfActions(user, UserActions.REGISTRATION);
        System.out.println("Добро пожаловать " + name + "\n");
    }

    /**
     * Авторизация пользователя
     * @param login логин пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public void authorizationUser(String login, String password) {
        if (userMap.containsKey(login)) {
            User user = userMap.get(login);
            if (user.getPassword().equals(password)) {
                auditOfActions(user, UserActions.AUTHORIZATION);
                System.out.println("Добро пожаловать " + user.getName() + "\n");
            } else {
                System.out.println("Ошибка входа " + "\n");
            }
        }
    }

    /**
     * Просмотр текущего баланса
     * @param user пользователь.
     * @return баланс на счету.
     */
    @Override
    public double currentBalance(User user) {
        auditOfActions(user, UserActions.GET_BALANCE);
        return user.getBalance();
    }

    /**
     * Снятие денег со счета
     * @param login логин пользователя.
     * @param sum запрашиваемая сумма.
     */
    @Override
    public void withdrawalOfFunds(String login, double sum) {
        User user = userMap.get(login);
        try {
            if (currentBalance(user) >= 0 && sum <= currentBalance(user)) {
                user.setBalance(user.getBalance() - sum);
                auditOfActions(user, UserActions.CREDIT);
                replenishmentHistory(user, TransactionType.CREDIT, sum);
            } else {
                throw new IllegalArgumentException("Недостаточно средств");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            auditOfActions(user, UserActions.FATAL);
        }
    }

    /**
     * Пополнение баланса
     * @param login логин пользователя.
     * @param sum сумма пополнения.
     */
    @Override
    public void balanceReplenishment(String login, double sum) {
        User user = userMap.get(login);
        user.setBalance(user.getBalance() + sum);
        auditOfActions(user, UserActions.DEBIT);
        replenishmentHistory(user, TransactionType.DEBIT, sum);
    }

    /**
     * История транзакций
     * @param user пользователь.
     * @param transactionType тип транзакции.
     * @param sum сумма транзакции.
     * @return список транзакций.
     */
    @Override
    public List<Transaction> replenishmentHistory(User user, TransactionType transactionType, double sum) {
        LocalDateTime time = LocalDateTime.now();
        transactions.add(new Transaction(++GENERATE_ID_TRANSACTION, user, transactionType, sum, time));
        user.setTransactions(transactions);
        return transactions;
    }

    /**
     * Аудит действий пользователя
     * @param user пользователь.
     * @param userActions действие пользователя.
     * @return список действий пользователя.
     */
    @Override
    public List<LogEntry> auditOfActions(User user, UserActions userActions) {
        LocalDateTime time = LocalDateTime.now();
        listActions.add(new LogEntry(++GENERATE_ID_ACTION, userActions, time, user));
        user.setActionLog(listActions);
        return listActions;
    }
}
