package com.application.web.requests;

import com.application.tools.EncryptionHandler;

public class SignInRequest {

    String username;

    byte[] password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = new EncryptionHandler().encrypt(password);
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }
}
