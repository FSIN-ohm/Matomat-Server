package org.fsin.matomat.rest.model;

import java.util.Date;

public class Admin {
    public int id;
    public int user_id;
    public long balance;
    public Date last_seen;
    public boolean available;
    public String user_name;
    public String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Date getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(Date last_seen) {
        this.last_seen = last_seen;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
