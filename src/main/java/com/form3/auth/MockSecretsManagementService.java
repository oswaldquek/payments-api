package com.form3.auth;

import java.io.UnsupportedEncodingException;

public class MockSecretsManagementService implements SecretsManagementService {

    private final String jwtTokenSecret = "dfçzebdwdz772d632gds45643etrgsfdfweefer32gds";

    @Override
    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }
}
