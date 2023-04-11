package com.application.web.requests;

import com.application.utils.EncryptionHandler;

/**
 * Provides an object representing the encapsulation of the arguments necessary to perform signing in for retrieval of token
 * through a post action in the webservice. This object will be required in the body of the request.
 */
public class SignInRequest {

    private final String username;

    private final byte[] password;

    /**
     * This is the constructor with all the parameters needed for the post request
     * @param username String username
     * @param password String password
     */
    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = new EncryptionHandler().encrypt(password);
    }

    //GETTERS
    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }
}
