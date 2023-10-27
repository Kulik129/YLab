package io.ylab.domain.service;


/**
 * Интерфейс основной бизнес логики.
 */
public interface UserService {
    /**
     * Регистрация пользователя в приложении.
     *
     * @param name     имя пользователя.
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     */
    boolean registrationUser(String name, String login, String password);

    /**
     * Аутентификация пользователя.
     *
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     */
    boolean authorizationUser(String login, String password);

    /**
     * Просмотр текущего баланса пользователя.
     *
     * @param login пользователя.
     * @return
     */
    double currentBalance(String login);

    /**
     * Снятие ДС со счета.
     *
     * @param login логин пользователя.
     * @param sum   запрашиваемая сумма для снятия.
     */
    void withdrawalOfFunds(String login, double sum);

    /**
     * Пополнение счета.
     *
     * @param login логин пользователя.
     * @param sum   сумма пополнения.
     */
    void balanceReplenishment(String login, double sum);

    /**
     * Лист транзакций пользователя.
     *
     * @param login пользователя.
     */
    void replenishmentHistory(String login);

    /**
     * Лист действий пользователя.
     */
    void auditOfActions();
}
