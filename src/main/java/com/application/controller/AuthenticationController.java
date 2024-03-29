package com.application.controller;

import com.application.persistence.DatabaseManager;
import com.application.utils.EncryptionHandler;

import java.util.HashMap;
import java.util.UUID;

/**
 * Provides an object that acts as an intermediary between the web service and the persistence for user related methods. It performs the
 * actions necessary to fulfill the requests of the web service. It employs some nested classes.
 */
public class AuthenticationController {

    private final DatabaseManager databaseManager;
    private final HashMap<UUID,UsernameAndAdminStatus> userTokenMap;
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
     * @return boolean informing if the insertion has been properly completed
     */
    public boolean createNewUser(String username,byte[] password,boolean adminStatus){

        return databaseManager.createNewUser(username,password,adminStatus);

    }

    /**
     * Method to verify the credentials of a user and password through the retrieval of the username corresponding password and
     * comparison with the provided password
     * @param username String username used to find the password in the database
     * @param password Array of byte corresponding to an encryption of the password provided by the user
     * @return UUID object depending of the succes of the verification. It can return null or a random UUID that will serve as
     * a token for further authentication
     */
    public UUID logIn(String username, byte[] password){

        EncryptedPasswordAndAdminStatus passwordAndAdminStatusFromDatabase = databaseManager.getPasswordAndAdminStatus(username);

        if(passwordAndAdminStatusFromDatabase == null) return null;

        String decryptedInsertedPassword = new EncryptionHandler().decrypt(password);
        String decryptedRetrievedPassword = new EncryptionHandler().decrypt(passwordAndAdminStatusFromDatabase.getEncryptedPassword());

        boolean authenticationCheck = decryptedInsertedPassword.equals(decryptedRetrievedPassword);

        if(authenticationCheck){

            //Previous UUID tokens assigned to this user stored in maps are cleared
            UUID oldUUID = mirrorUserTokenMap.get(username);
            userTokenMap.remove(oldUUID);

            UUID currentUserUUID = UUID.randomUUID();
            UsernameAndAdminStatus usernameAndAdminStatus = new UsernameAndAdminStatus(username,passwordAndAdminStatusFromDatabase.isAdminStatus());
            userTokenMap.put(currentUserUUID,usernameAndAdminStatus);
            mirrorUserTokenMap.put(username,currentUserUUID);
            return currentUserUUID;
        } else {
            return null;
        }
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
        return userTokenMap.get(token).getUsername();
    }

    /**
     * This method verifies that the client of the webservice particular method has signed in first, is using a token UUID
     * that has been stored before and is an Admin by comparing the provided UUID to those stored in the map attribute and return the corresponding username.
     * Avoiding constant credential verification with username and password
     * @param token UUID object which should be the token UUID provided when it was last signed in.
     * @return boolean returning true or false whether the UUID provided corresponds to a user that is an admin or not
     */
    public boolean adminAuthentication(UUID token){
        if(token == null) return false;
        return userTokenMap.get(token).isAdminStatus();
    }

    /**
     * Method to connect the retrieval request of the corresponding manufacturer name by the controller with the class in charged
     * of interacting with the database.
     * @param username String username of the corresponding manufacturer name desired
     * @return Array list of Strings with the names of the manufacturers this username can perform modifications to and related beers
     */
    public boolean checkAuthorization(String username, String manufacturerName){

        if(username == null) return false;
        if(userTokenMap.get(mirrorUserTokenMap.get(username)).isAdminStatus()) return true;
        if(manufacturerName == null) return false;

        return databaseManager.checkManufacturerNameForAuthorization(username,manufacturerName);
    }

    //Private Classes to hold two objects:

    private class UsernameAndAdminStatus {

        String username;

        boolean adminStatus;

        public UsernameAndAdminStatus(String username, boolean adminStatus) {
            this.username = username;
            this.adminStatus = adminStatus;
        }

        public String getUsername() {
            return username;
        }

        public boolean isAdminStatus() {
            return adminStatus;
        }
    }

    /**
     * Nested class required to encapsulate username and admin status information
     */
    public static class EncryptedPasswordAndAdminStatus {

        byte[] encryptedPassword;

        boolean adminStatus;

        public EncryptedPasswordAndAdminStatus(byte[] encryptedPassword, boolean adminStatus) {
            this.encryptedPassword = encryptedPassword;
            this.adminStatus = adminStatus;
        }

        public byte[] getEncryptedPassword() {
            return encryptedPassword;
        }

        public boolean isAdminStatus() {
            return adminStatus;
        }

        public void setEncryptedPassword(byte[] encryptedPassword) {
            this.encryptedPassword = encryptedPassword;
        }

        public void setAdminStatus(boolean adminStatus) {
            this.adminStatus = adminStatus;
        }
    }
}
