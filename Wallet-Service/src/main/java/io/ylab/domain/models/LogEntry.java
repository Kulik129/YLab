package io.ylab.domain.models;

import io.ylab.domain.action.UserActions;

import java.time.LocalDateTime;

/**
 * Класс описывающий действия пользователя
 */
public class LogEntry {
    /**
     * Уникальный идентификатор.
     */
    private int id;
    /**
     * Пользователь.
     */
    private User user;
    /**
     * Действия.
     */
    private UserActions userActions;
    /**
     * Дата и время
     */
    private LocalDateTime localDateTime;

    //regionGettersSetters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserActions getUserActions() {
        return userActions;
    }

    public void setUserActions(UserActions userActions) {
        this.userActions = userActions;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion

    //regionConstructor

    public LogEntry(int id, UserActions userActions, LocalDateTime localDateTime,User user) {
        this.id = id;
        this.userActions = userActions;
        this.localDateTime = localDateTime;
        this.user = user;
    }
    //endregion

    //regionToString

    @Override
    public String toString() {
        return "Действия пользователя{ " +
                "login: " + user.getLogin() +
                ", actions: " + userActions +
                ", date and time: " + localDateTime +
                " "+
                '}';
    }


    //endregion
}
