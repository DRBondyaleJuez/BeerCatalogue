package com.application.controller;

import com.application.persistence.DatabaseManager;
import com.application.tools.EncryptionHandler;

import java.util.HashMap;
import java.util.UUID;

public class AuthenticationController {

    private final DatabaseManager databaseManager;
    private final HashMap<UUID,String> userTokenMap;
    private final HashMap<String,UUID> mirrorUserTokenMap;

    /**
     * This is the constructor. Here  an instance of the DatabaseManager class is assigned to the databaseManager attribute.
     */
    public AuthenticationController() {

        databaseManager = DatabaseManager.getInstance();
        userTokenMap = new HashMap<>();
        mirrorUserTokenMap = new HashMap<>();
    }

    public boolean createNewUser(String username,byte[] password,boolean adminStatus, String manufacturerName){

        return databaseManager.createNewUser(username,password,adminStatus,manufacturerName);

    }

    public UUID signIn(String username, byte[] password){

        byte[] passwordFromDatabase = databaseManager.getPassword(username);

        if(passwordFromDatabase == null) return null;

        String decryptedInsertedPassword = new EncryptionHandler().decrypt(password);
        String decryptedRetrievedPassword = new EncryptionHandler().decrypt(passwordFromDatabase);

        boolean authenticationCheck = decryptedInsertedPassword.equals(decryptedRetrievedPassword);

        if(authenticationCheck){

            //Previous UUID tokens assigned to this user stored in maps are cleared
            cleanUserTokensFromMaps(username);

            UUID currentUserUUID = UUID.randomUUID();
            userTokenMap.put(currentUserUUID,username);
            mirrorUserTokenMap.put(username,currentUserUUID);
            return currentUserUUID;
        } else {
            return null;
        }
    }

//    public UUID signIn(String username, byte[] password){
//
//        byte[] passwordFromDatabase = databaseManager.getPassword(username);
//
//        boolean authenticationCheck = password.equals(passwordFromDatabase);
//
//        if(authenticationCheck){
//
//            //Previous UUID tokens assigned to this user stored in maps are cleared
//            cleanUserTokensFromMaps(username);
//
//            UUID currentUserUUID = UUID.randomUUID();
//            userTokenMap.put(currentUserUUID,username);
//            mirrorUserTokenMap.put(username,currentUserUUID);
//            return currentUserUUID;
//        } else {
//            return null;
//        }
//    }

    private void cleanUserTokensFromMaps(String username) {
        if(username == null) return;
        UUID oldUUID = mirrorUserTokenMap.get(username);
        if(oldUUID == null) return;

        userTokenMap.remove(oldUUID,username);
        mirrorUserTokenMap.remove(username,oldUUID);
    }

    public String tokenAuthentication(UUID token){
        if(token == null) return null;
        return userTokenMap.get(token);
    }


}
