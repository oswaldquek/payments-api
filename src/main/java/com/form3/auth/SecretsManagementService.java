package com.form3.auth;

import java.io.UnsupportedEncodingException;

public interface SecretsManagementService {
    byte[] getJwtTokenSecret() throws UnsupportedEncodingException;
}
