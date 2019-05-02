package org.fsin.matomat.rest.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.AdminEntry;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class Authenticator implements AuthenticationProvider {
    private static Authenticator authenticator = null;

    private enum Rolle {
        USER,
        ADMIN
    }

    private class User {
        public User(int id, byte[] password, byte[] salt, Rolle rolle) {
            this.id = id;
            this.password = password;
            this.rolle = rolle;
            this.salt = salt;
        }

        public int getId() {
            return id;
        }

        public byte[] getPassword() {
            return password;
        }

        public Rolle getRolle() {
            return rolle;
        }

        public byte[] getSalt() {
            return salt;
        }

        private int id;
        private byte[] password;
        private Rolle rolle;
        private byte[] salt;
    }

    private HashMap<String, User> users = null;

    public static Authenticator getInstance() {
        if(authenticator == null){
            authenticator = new Authenticator();
            authenticator.invalidate();
        }
        return authenticator;
    }

    private Authenticator() {

    }

    public void invalidate() {
        try {
            Database db = Database.getInstance();
            users = new ManagedMap<>();

            for(UserEntry entry : db.usersGetAll(0, 1000000, true)) {
                if(entry.getId() > 2) // filter out default users as they are not supposed to be login capable
                    users.put(new String(removeTailingZeros(entry.getAuthHash())),
                            new User(entry.getId(),
                                    new byte[]{},
                                    new byte[]{},
                                    Rolle.USER));
            }

            for(AdminEntry entry : db.adminGetAll(0, 10000000, true)) {
                users.put(entry.getUsername(),
                        new User(entry.getId(), entry.getPassword(), entry.getPasswordSalt(), Rolle.ADMIN));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not invalidate login cache", e);
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        try {
            User user = users.get(authentication.getName());
            if (user.getRolle() == Rolle.ADMIN) {
                String password = (String) authentication.getCredentials();
                if (Arrays.equals(DigestUtils.sha512((new String(user.getSalt()) + password).getBytes()),
                        user.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(
                            authentication.getName(), authentication.getCredentials().toString(), new ArrayList<>());
                }
            }
            if(user.getRolle() == Rolle.USER) {
                return new UsernamePasswordAuthenticationToken(
                        authentication.getName(), authentication.getCredentials().toString(), new ArrayList<>());
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

    /** -------------- UTILS -------------------- **/
    private byte[] removeTailingZeros(byte[] input) {
        int firstNonZero = input.length-1;
        while(input[firstNonZero] == 0
            && firstNonZero != 0) firstNonZero--;
        return Arrays.copyOfRange(input, 0, firstNonZero+1);
    }
}
