package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.sql.Date;

public class TransferEntry {
    private int transaction_id;
    private Date date;
    private int sender_id;
    private int receiver_id;
    private BigDecimal charched_amount;

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public BigDecimal getCharched_amount() {
        return charched_amount;
    }

    public void setCharched_amount(BigDecimal charched_amount) {
        this.charched_amount = charched_amount;
    }
}
