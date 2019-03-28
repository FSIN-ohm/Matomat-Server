package org.fsin.matomat.database.model;

import java.sql.Date;

public class UserEntry {
    private Integer id;
    private byte[] authHash;
    private Integer balance;
    private Date lastSeen;
    private boolean available;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getAuthHash() {
        return authHash;
    }

    public void setAuthHash(byte[] authHash) {
        this.authHash = authHash;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
