package com.application.web.requests;

import java.util.UUID;

public class CreateAdminRequest extends CreateNewUserRequest{

    UUID authenticationToken;

    /**
     * This is the constructor with all the parameters needed for the put request
     * @param newUsername String the username
     * @param password    String the password
     * @param authenticationToken UUID corresponding to the authentication token of the user. Only Admin Users can create new admin
     */
    public CreateAdminRequest(String newUsername, String password, UUID authenticationToken) {
        super(newUsername, password);
        super.setAdminStatus(true);
        this.authenticationToken = authenticationToken;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }
}
