package org.fsin.matomat.rest.model;

public class UserUpdate {
    private String auth_hash;
    private String name;

    public String getAuth_hash() {
        return auth_hash;
    }

    public void setAuth_hash(String auth_hash) {
        this.auth_hash = auth_hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
