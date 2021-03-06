package org.fsin.matomat.rest.model;

import java.util.Date;

public class Transaction {
    private int id;
    private Date date;
    private long sender;
    private long receiver;
    private int amount;
    private String transaction_type;

    public Transaction() {

    }

    public Transaction(Transaction t) {
        this.id = t.id;
        this.date = t.date;
        this.sender = t.sender;
        this.receiver = t.receiver;
        this.amount = t.amount;
        this.transaction_type = t.transaction_type;
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

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
}
