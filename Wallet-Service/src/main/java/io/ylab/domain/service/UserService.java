package io.ylab.domain.service;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.User;
import io.ylab.infrastructure.repository.UserRepository;


/**
 * Основная бизнес логика.
 * Сервис обработки основных методов.
 */

public class UserService implements ServiceUser {
//    /**
//    /**
//     * Для автоматической генерации ID пользователя.
//     */
//    private static int CREATE_USER_ID;
//    /**
//     *  Для автоматической генерации ID действия пользователя.
//     */
//    private static int GENERATE_ID_ACTION;
//    /**
//     * Для автоматической генерации ID действия.
//     */
//    private static int GENERATE_ID_TRANSACTION;
//    /**
//     * Пользователи.
//     */
//    public Map<String, User> userMap = new HashMap<>();
//    /**
//     * Действия.
//     */
//    public List<LogEntry> listActions = new ArrayList<>();
//    /**
//     * Транзакции.
//     */
//    public List<Transaction> transactions = new ArrayList<>();
//
//    /**
//     * Пользователи для тестирования.
//     */
//
//    {
//        userMap.put("admin", new User(++CREATE_USER_ID, "Dmitrii", "admin", "1234"));
//        userMap.put("vik", new User(++CREATE_USER_ID, "Viktor", "vik", "4321"));
//    }
//
//

    private UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    /**
     * Регистрация пользователя
     *
     * @param name     имя пользователя.
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public boolean registrationUser(String login, String password, String name) {
        if (!repository.userPresence(login)) {
            User user = new User(repository.getUserMap().size() + 1, name, login, password);
            repository.addUserInMap(user);
            repository.addLogEntry(user, UserActions.REGISTRATION);
            System.out.println("Добро пожаловать " + name + "\n");
            return true;
        }
        System.out.println("Пользователь с таким логином уже зарегистрирован");
        return false;
    }

    /**
     * Авторизация пользователя
     *
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     */
    @Override
    public boolean authorizationUser(String login, String password) {
        if (repository.userPresence(login)) {
            User user = repository.getUser(login);
            if (user.getPassword().equals(password)) {
                repository.addLogEntry(user, UserActions.AUTHORIZATION);
                System.out.println("Добро пожаловать " + user.getName() + "\n");
                return true;
            } else {
                repository.addLogEntry(user, UserActions.FATAL);
                return false;
            }
        }
        return false;
    }

    /**
     * Просмотр текущего баланса
     *
     * @param login пользователя.
     * @return баланс на счету.
     */
    @Override
    public double currentBalance(String login) {
        User user = repository.getUser(login);
        repository.addLogEntry(user, UserActions.GET_BALANCE);
        return user.getBalance();
    }

    /**
     * Снятие денег со счета
     *
     * @param login логин пользователя.
     * @param sum   запрашиваемая сумма.
     */
    @Override
    public void withdrawalOfFunds(String login, double sum) {
        User user = repository.getUser(login);
        try {
            if (currentBalance(user.getLogin()) >= 0 && sum <= currentBalance(user.getLogin())) {
                user.setBalance(user.getBalance() - sum);
                repository.addLogEntry(user, UserActions.DEBIT);
                repository.addTransaction(user, TransactionType.DEBIT, sum);

            } else {
                throw new IllegalArgumentException("Недостаточно средств");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            repository.addLogEntry(user, UserActions.FATAL);
        }
    }

    /**
     * Пополнение баланса
     *
     * @param login логин пользователя.
     * @param sum   сумма пополнения.
     */
    @Override
    public void balanceReplenishment(String login, double sum) {
        User user = repository.getUser(login);
        user.setBalance(user.getBalance() + sum);
        System.out.println("Ваш баланс: " + user.getBalance() + "\n");
        repository.addLogEntry(user, UserActions.DEBIT);
        repository.addTransaction(user, TransactionType.DEBIT, sum);
    }

    /**
     * История транзакций.
     *
     * @param login логин пользователя.
     */
    @Override
    public void replenishmentHistory(String login) {
        User user = repository.getUser(login);
        repository.getTransaction(user);
    }

    /**
     * Аудит действий пользователя
     */
    @Override
    public void auditOfActions() {
        for (LogEntry log : repository.getListActions()) {
            System.out.println(log);
        }
    }
}
