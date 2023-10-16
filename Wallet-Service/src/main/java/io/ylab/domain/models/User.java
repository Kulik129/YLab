package io.ylab.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс описывающий пользователя.
 */
public class User {
    /**
     * Уникальный идентификатор.
     */
    private int id;
    /**
     * Имя пользователя.
     */
    private String name;
    /**
     * Логин пользователя.
     */
    private String login;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Текущий баланс пользователя.
     */
    private double balance;
    /**
     * Список транзакций пользователя.
     */
    private List<Transaction> transactions;
    /**
     * Лог действий пользователя.
     */
    private List<LogEntry> actionLog;

    //regionGettersSetters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<LogEntry> getActionLog() {
        return actionLog;
    }

    public void setActionLog(List<LogEntry> actionLog) {
        this.actionLog = actionLog;
    }

    //endregion

    //regionConstructor

    public User(int id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.transactions = new ArrayList<>();
        this.actionLog = new ArrayList<>();
    }

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    //endregion

    //regionToString

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                ", actionLog=" + actionLog +
                '}';
    }
    //endregion

    //regionEqualsHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, password, balance, transactions, actionLog);
    }

    //endregion
}
