package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class PurchaseEntry {
    private int transaction_id;
    private Date date;
    private int sender_id;
    private int recipient_id;
    private BigDecimal charge_amount;

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

    public int getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(int recipient_id) {
        this.recipient_id = recipient_id;
    }

    public BigDecimal getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(BigDecimal charge_amount) {
        this.charge_amount = charge_amount;
    }
}
