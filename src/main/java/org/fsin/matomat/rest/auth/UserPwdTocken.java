package org.fsin.matomat.rest.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserPwdTocken extends UsernamePasswordAuthenticationToken {

    private final int id;
    private final User.Role role;

    public UserPwdTocken(int id, User.Role role, Object principal, Object credentials) {
        super(principal, credentials);
        this.id = id;
        this.role = role;
    }

    public UserPwdTocken(int id, User.Role role, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public User.Role getRole() {
        return role;
    }
}
