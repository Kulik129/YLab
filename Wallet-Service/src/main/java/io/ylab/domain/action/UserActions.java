package io.ylab.domain.action;

/**
 * Перечисление действий пользователя.
 */
public enum UserActions {
    REGISTRATION,
    AUTHORIZATION,
    GET_BALANCE,
    CREDIT, // зачисление
    DEBIT, // списание средств со счета
    FATAL,
    HISTORY
}
