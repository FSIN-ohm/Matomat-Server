package org.fsin.matomat.database.model;

import java.sql.Date;

public class UserEntry {
    private Integer id;
    private String authHash;
    private Integer balance;
    private Date lastSeen;
    private Boolean avialable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthHash() {
        return authHash;
    }

    public void setAuthHash(String authHash) {
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

    public Boolean getAvialable() {
        return avialable;
    }

    public void setAvialable(Boolean avialable) {
        this.avialable = avialable;
    }
}
