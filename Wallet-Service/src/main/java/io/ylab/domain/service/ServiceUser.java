package io.ylab.domain.service;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;

import java.util.List;

/**
 * Интерфейс основной бизнес логики.
 */
public interface ServiceUser {
    /**
     * Регистрация пользователя в приложении.
     * @param name имя пользователя.
     * @param login логин пользователя.
     * @param password пароль пользователя.
     */
    void registrationUser(String name, String login, String password);

    /**
     * Аутентификация пользователя.
     * @param login логин пользователя.
     * @param password пароль пользователя.
     */
    void authorizationUser(String login, String password);

    /**
     * Просмотр текущего баланса пользователя.
     * @param user передаваемый пользователь.
     * @return
     */
    double currentBalance(User user);

    /**
     * Снятие ДС со счета.
     * @param login логин пользователя.
     * @param sum запрашиваемая сумма для снятия.
     */
    void withdrawalOfFunds(String login, double sum);

    /**
     * Пополнение счета.
     * @param login логин пользователя.
     * @param sum сумма пополнения.
     */
    void balanceReplenishment(String login, double sum);

    /**
     * Лист транзакций пользователя.
     * @param user передаваемый пользователь.
     * @param transactionType тип транзакции.
     * @param sum сумма транзакции.
     * @return список транзакций.
     */
    List<Transaction> replenishmentHistory(User user, TransactionType transactionType, double sum);

    /**
     * Лист действий пользователя.
     * @param user передаваемый пользователь.
     * @param userActions тип действия пользователя.
     * @return спиков действий пользователя.
     */
    List<LogEntry> auditOfActions(User user, UserActions userActions);
}
