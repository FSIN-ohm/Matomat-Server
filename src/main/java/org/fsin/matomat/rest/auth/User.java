package org.fsin.matomat.rest.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public class User {
    public enum Role {
        USER,
        ADMIN,
        DEVICE
    }

    private final int id;
    private final byte[] password;
    private final Role role;
    private final byte[] salt;

    public User(int id, byte[] password, byte[] salt, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public byte[] getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public byte[] getSalt() {
        return salt;
    }
}