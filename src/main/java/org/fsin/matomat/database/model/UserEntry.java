package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserEntry {
    private Integer id;
    private byte[] authHash;
    private BigDecimal balance;
    private Timestamp lastSeen;
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

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
