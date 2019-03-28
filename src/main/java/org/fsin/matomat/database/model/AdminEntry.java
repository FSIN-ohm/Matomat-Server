package org.fsin.matomat.database.model;

public class AdminEntry {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String passwordSalt;
    private int corespondingUserId;
    private boolean isAvailable;

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
}
