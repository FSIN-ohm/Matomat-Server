package org.fsin.matomat;

public class CreateUserKeyAuthenticator {

    private static CreateUserKeyAuthenticator authKeyReader = null;

    private CreateUserKeyAuthenticator() {

    }

    public static CreateUserKeyAuthenticator getInstance() {
        if(authKeyReader == null) {
            authKeyReader = new CreateUserKeyAuthenticator();
        }
        return authKeyReader;
    }

    public boolean isAcceptable(String authKey) {
        // Here the logig for reading and checking against the auth key.txt should be done
        if(authKey == "asdfasdf") return true;
        else return true;
    }
}
