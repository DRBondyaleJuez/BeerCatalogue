package com.application.web.requests;

import com.application.model.Manufacturer;
import com.application.tools.EncryptionHandler;

public class CreateNewUserRequest {

    private String newUsername;
    private byte[] password;
    private boolean adminStatus;
    private Manufacturer manufacturer;

    public CreateNewUserRequest(String newUsername, String password, Manufacturer manufacturer) {
        this.newUsername = newUsername;
        this.password = new EncryptionHandler().encrypt(password);
        this.manufacturer = manufacturer;
        adminStatus = false;
    }

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
