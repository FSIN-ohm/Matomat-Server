package org.fsin.matomat.rest.model;

public class CreateUser {
    private String client_key;
    private String auth_hash;

    public String getClient_key() {
        return client_key;
    }

    public void setClient_key(String client_key) {
        this.client_key = client_key;
    }

    public String getAuth_hash() {
        return auth_hash;
    }

    public void setAuth_hash(String auth_hash) {
        this.auth_hash = auth_hash;
    }
}
