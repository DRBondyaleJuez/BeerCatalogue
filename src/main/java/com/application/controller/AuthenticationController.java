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
     * The hashmaps for temporal storage of authentication information are also instantiated here.
     */
    public AuthenticationController() {

        databaseManager = DatabaseManager.getInstance();
        userTokenMap = new HashMap<>();
        mirrorUserTokenMap = new HashMap<>();
    }

    /**
     * Method to petition to the database the insertion of a new user in the corresponding table
     * @param username String username that will correspond to the primary key. Must be unique
     * @param password Array of byte the encrypted password
     * @param adminStatus boolean informing whether this is an admin or not
     * @param manufacturerName String the name of the manufacturer this username has access to
     * @return boolean informing if the insertion has been properly completed
     */
    public boolean createNewUser(String username,byte[] password,boolean adminStatus, String manufacturerName){

        return databaseManager.createNewUser(username,password,adminStatus,manufacturerName);

    }

    /**
     * Method to verify the credentials of a user and password through the retrival of the username corresponding password and
     * comparison with the provided password
     * @param username String username used to find the password in the database
     * @param password Array of byte corresponding to an encryption of the password provided by the user
     * @return UUID object depending of the succes of the verification. It can return null or a random UUID that will serve as
     * a token for further authentication
     */
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

    /**
     * This method verifies that the client of the webservice particular method has signed in first and is using a token UUID
     * that has been stored before by comparing to those stored in the map attribute and return the corresponding username.
     * Avoiding constant credential verification with username and password
     * @param token UUID object which should be the token UUID provided when it was last signed in.
     * @return String value in the map associated with the token key which should correspond to the correct username.
     */
    public String tokenAuthentication(UUID token){
        if(token == null) return null;
        return userTokenMap.get(token);
    }


}
