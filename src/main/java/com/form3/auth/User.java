package com.form3.auth;

import java.security.Principal;

public class User implements Principal {

    private final String id;
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
