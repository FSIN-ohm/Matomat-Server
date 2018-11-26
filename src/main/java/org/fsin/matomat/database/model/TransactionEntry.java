package org.fsin.matomat.database.model;

import java.sql.Date;
import java.util.List;

public class TransactionEntry {
    public enum Type {
        IN,
        OUT,
        TRANSFARE
    }

    private Integer id;
    private Date date;
    private Type type;
    private Integer charchedAmount;
    private UserEntry sender;
    private UserEntry recipient;

    private List<ProductsEntry> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getCharchedAmount() {
        return charchedAmount;
    }

    public void setCharchedAmount(Integer charchedAmount) {
        this.charchedAmount = charchedAmount;
    }

    public UserEntry getSender() {
        return sender;
    }

    public void setSender(UserEntry sender) {
        this.sender = sender;
    }

    public UserEntry getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntry recipient) {
        this.recipient = recipient;
    }

    public List<ProductsEntry> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsEntry> products) {
        this.products = products;
    }
}
