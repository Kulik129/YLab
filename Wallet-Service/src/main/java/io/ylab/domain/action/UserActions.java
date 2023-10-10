package io.ylab.domain.action;

/**
 * Перечисление действий пользователя.
 */
public enum UserActions {
    REGISTRATION,
    AUTHORIZATION,
    LOGOUT,
    GET_BALANCE,
    CREDIT, // снятие денег
    DEBIT, // пополнение счета
    FATAL,
    HISTORY
}
