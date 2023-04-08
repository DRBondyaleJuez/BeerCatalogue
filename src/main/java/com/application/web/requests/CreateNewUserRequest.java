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
    private Manufacturer manufacturer;

    /**
     * This is the constructor with all the parameters needed for the post request
     * @param newUsername String the username
     * @param password String the password
     * @param manufacturer Manufacturer object corresponding the manufacturer the user will have access to and to the related beers
     */
    public CreateNewUserRequest(String newUsername, String password, Manufacturer manufacturer) {
        this.newUsername = newUsername;
        this.password = new EncryptionHandler().encrypt(password);
        this.manufacturer = manufacturer;
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

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
