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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class Authenticator implements AuthenticationProvider {
    private static Authenticator authenticator = null;
    private String deviceKeyFile = null;


    private HashMap<String, User> users = null;

    public static void init(String keyFile) {
        if(authenticator == null) {
            authenticator = new Authenticator();
        }
        authenticator.deviceKeyFile = keyFile;
        authenticator.invalidate();
    }

    public static Authenticator getInstance() {
        if(authenticator == null || authenticator.deviceKeyFile == null) {
            throw new RuntimeException("init() has not been called on authenticator");
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
                                    User.Role.USER));
            }

            for(AdminEntry entry : db.adminGetAll(0, 10000000, true)) {
                users.put(entry.getUsername(),
                        new User(entry.getId(),
                                entry.getPassword(),
                                entry.getPasswordSalt(),
                                User.Role.ADMIN));
            }

            for(String[] tuple : readDeviceKeyFile(deviceKeyFile)) {
                users.put(tuple[1],
                        new User(-1,
                                new byte[]{},
                                new byte[]{},
                                User.Role.DEVICE));
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
            if (user.getRole() == User.Role.ADMIN) {
                String password = (String) authentication.getCredentials();
                if (Arrays.equals(DigestUtils.sha512((new String(user.getSalt()) + password).getBytes()),
                        user.getPassword())) {
                    return new UserPwdTocken (
                            user.getId(),
                            user.getRole(),
                            authentication.getName(),
                            authentication.getCredentials().toString(),
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                }
            }
            if(user.getRole() == User.Role.USER) {
                return new UserPwdTocken (
                        user.getId(),
                        user.getRole(),
                        authentication.getName(),
                        authentication.getCredentials().toString(),
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            }

            if(user.getRole() == User.Role.DEVICE) {
                return new UserPwdTocken (
                        user.getId(),
                        user.getRole(),
                        authentication.getName(),
                        authentication.getCredentials().toString(),
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_DEVICE")));
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


    private List<String[]> readDeviceKeyFile(String fileName) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));

            String line = "";
            ArrayList<String[]> content = new ArrayList<>();
            while((line = file.readLine()) != null) {
                content.add(line.split(":"));
            }
            file.close();
            return content;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
