package com.form3.auth;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MockUserValidationService implements UserValidationService {

    private Map<String, String> users = ImmutableMap.of("Joe Bloggs", "p4ssw0rd");
    @Override
    public boolean login(String userName, String password) {
        if (users.get(userName) != null && users.get(userName).equals(password)) {
            return true;
        }
        return false;
    }
}
