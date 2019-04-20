package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionEntry {

    public enum TransactionType {
        PURCHASE("purchase"),
        ORDER("order"),
        DEPOSIT("deposit"),
        WITHDRAW("withdraw"),
        TRANSFERE("transfere"),
        ANY("any");

        private String value;

        TransactionType (String v) {
            value = v;
        }

        public String value() {
            return value;
        }
    }

    private int id;
    private Date date;
    private int senderId;
    private int recipientId;
    private BigDecimal amount;
    private TransactionType type;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
