package com.application.controller;

import com.application.persistence.DatabaseManager;

import java.util.HashMap;
import java.util.UUID;

public class AuthorizationController {

    private final DatabaseManager databaseManager;

    /**
     * This is the constructor. Here  an instance of the DatabaseManager class is assigned to the databaseManager attribute.
     */
    public AuthorizationController() {
        databaseManager = DatabaseManager.getInstance();
    }

    public boolean checkAuthorization(String username, String manufacturerName){

        if(username == null) return false;

        String returnedManufacturerName = databaseManager.checkManufacturerNameForAuthorization(username);
        if(returnedManufacturerName.equals("admin") && username.equals("userAdmin")) return true;
        return manufacturerName.equals(returnedManufacturerName);
    }


}
