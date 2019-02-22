package org.fsin.matomat.database.model;

public class AdminEntry {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String password_salt;
    private int corespondingUser_id;

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

    public String getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public int getCorespondingUser_id() {
        return corespondingUser_id;
    }

    public void setCorespondingUser_id(int corespondingUser_id) {
        this.corespondingUser_id = corespondingUser_id;
    }
}
