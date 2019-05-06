package org.fsin.matomat.rest.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserPwdTocken extends UsernamePasswordAuthenticationToken {

    private final int id;

    public UserPwdTocken(int id, Object principal, Object credentials) {
        super(principal, credentials);
        this.id = id;
    }

    public UserPwdTocken(int id, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
