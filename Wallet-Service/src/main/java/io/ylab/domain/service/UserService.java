package io.ylab.domain.service;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.User;
import io.ylab.infrastructure.repository.UserRepository;


/**
 * Основная бизнес логика.
 * Сервис обработки основных методов.
 */

public class UserService implements ServiceUser {

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
            User user = new User(name, login, password);
            repository.saveUser(user);
            repository.saveAction(user, UserActions.REGISTRATION);
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
            User user = repository.findByLogin(login);
            if (user.getPassword().equals(password)) {
                repository.saveAction(user, UserActions.AUTHORIZATION);
                System.out.println("Добро пожаловать " + user.getName() + "\n");
                return true;
            } else {
                repository.saveAction(user, UserActions.FATAL);
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
        User user = repository.findByLogin(login);
        repository.saveAction(user, UserActions.GET_BALANCE);
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
        User user = repository.findByLogin(login);
        try {
            if (currentBalance(user.getLogin()) >= 0 && sum <= currentBalance(user.getLogin())) {
                user.setBalance(user.getBalance() - sum);
                repository.updateBalance(user);
                repository.saveAction(user, UserActions.DEBIT);
                repository.saveTransaction(user, TransactionType.DEBIT, sum);

            } else {
                throw new IllegalArgumentException("Недостаточно средств");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            repository.saveAction(user, UserActions.FATAL);
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
        User user = repository.findByLogin(login);
        user.setBalance(user.getBalance() + sum);
        repository.updateBalance(user);
        repository.saveAction(user, UserActions.CREDIT);
        repository.saveTransaction(user, TransactionType.CREDIT, sum);
    }

    /**
     * История транзакций.
     *
     * @param login логин пользователя.
     */
    @Override
    public void replenishmentHistory(String login) {
        User user = repository.findByLogin(login);
        repository.getTransaction(user);
        repository.saveAction(user, UserActions.HISTORY);
    }

    /**
     * Аудит действий пользователя
     */
    @Override
    public void auditOfActions() {
        repository.getAction();
    }
}
