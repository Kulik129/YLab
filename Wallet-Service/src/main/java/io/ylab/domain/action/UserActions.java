package io.ylab.domain.action;

/**
 * Перечисление действий пользователя.
 */
public enum UserActions {
    REGISTRATION,
    AUTHORIZATION,
    LOGOUT,
    GET_BALANCE,
    CREDIT, // пополнение счета
    DEBIT, // снятие денег
    FATAL,
    HISTORY
}
