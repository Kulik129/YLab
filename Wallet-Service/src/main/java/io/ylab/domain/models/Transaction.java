package io.ylab.domain.models;

import io.ylab.domain.action.TransactionType;

import java.time.LocalDateTime;

public class Transaction {
    /**
     * Уникальный идентификатор.
     */
    private int id;
    /**
     * Пользователь.
     */
    private User user;
    /**
     * Тип транзакции.
     */
    private TransactionType transactionType;
    /**
     * Сумма транзакции
     */
    private double sum;
    /**
     * Дата и время транзакции.
     */
    private LocalDateTime localDateTime;

    //regionGettersSetters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    //endregion

    //regionConstructor

    public Transaction(int id, User user, TransactionType transactionType, double sum, LocalDateTime localDateTime) {
        this.id = id;
        this.user = user;
        this.transactionType = transactionType;
        this.sum = sum;
        this.localDateTime = localDateTime;
    }

    public Transaction(int id, TransactionType transactionType, double sum, LocalDateTime localDateTime) {
        this.id = id;
        this.transactionType = transactionType;
        this.sum = sum;
        this.localDateTime = localDateTime;
    }


//endregion

    //regionToString

    @Override
    public String toString() {
        return "Транзакция {" +
                "Тип: " + transactionType +
                ", Сумма: " + sum +
                ", Время: " + localDateTime +
                '}';
    }

    //endregion
}
