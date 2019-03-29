package org.fsin.matomat.database.model;

import java.sql.Time;
import java.sql.Timestamp;

public class AdminEntry {
    private int id;
    private String username;
    private String password;
    private String email;
    private String passwordSalt;
    private int corespondingUserId;
    private boolean isAvailable;
    private Timestamp lastSeen;
    private int balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public int getCorespondingUserId() {
        return corespondingUserId;
    }

    public void setCorespondingUserId(int corespondingUserId) {
        this.corespondingUserId = corespondingUserId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
