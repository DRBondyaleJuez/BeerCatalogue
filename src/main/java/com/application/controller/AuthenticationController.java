package com.application.controller;

import com.application.persistence.DatabaseManager;

public class AuthenticationController {

    private final DatabaseManager databaseManager;

    /**
     * This is the constructor. Here  an instance of the DatabaseManager class is assigned to the databaseManager attribute.
     */
    public AuthenticationController() {
        databaseManager = DatabaseManager.getInstance();
    }

    public boolean createNewUser(String username,byte[] password,boolean adminStatus, String manufacturerName){

        return databaseManager.createNewUser(username,password,adminStatus,manufacturerName);

    }




}
