package org.fsin.matomat.database.model;

public class AdminEntry {
    private Integer id;
    private String username;
    private byte[] password;
    private String email;
    private byte[] password_salt;

    private UserEntry corespondingUser;

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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(byte[] password_salt) {
        this.password_salt = password_salt;
    }

    public UserEntry getCorespondingUser() {
        return corespondingUser;
    }

    public void setCorespondingUser(UserEntry corespondingUser) {
        this.corespondingUser = corespondingUser;
    }
}
