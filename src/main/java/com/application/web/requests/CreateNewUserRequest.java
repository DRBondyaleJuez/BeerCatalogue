package com.application.web.requests;

import com.application.model.Manufacturer;
import com.application.utils.EncryptionHandler;

/**
 * Provides an object representing the encapsulation of the arguments necessary to perform the insertion of a user account
 * information through a post action in the webservice. This object will be required in the body of the request.
 */
public class CreateNewUserRequest {

    private String newUsername;
    private byte[] password;
    private boolean adminStatus;

    /**
     * This is the constructor with all the parameters needed for the post request
     * @param newUsername String the username
     * @param password String the password
     */
    public CreateNewUserRequest(String newUsername, String password) {
        this.newUsername = newUsername;
        this.password = new EncryptionHandler().encrypt(password);
        adminStatus = false;
    }

    //GETTERS
    public String getNewUsername() {
        return newUsername;
    }

    public byte[] getPassword() {
        return password;
    }

    public boolean isAdminStatus() {
        return adminStatus;
    }

}
