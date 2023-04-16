package com.application.persistence;

import com.application.controller.AuthenticationController;
import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Provides objects of a class connecting the controller with the class in charged of interacting with the database. It follows
 * a singleton architecture so there is only one possible instance of this class.
 */
public class DatabaseManager {

    private final DatabaseTalker databaseTalker;
    private static DatabaseManager instance;


    //This is the private constructor where the databaseTalker implementation is initialized for a particular table
    private DatabaseManager(){
        databaseTalker = new PostgreSQLDBConnection("beer_catalogue");
        instance = null;
    }

    /**
     * Following the singleton architecture this is the method to use an instance of this class which will always be the instance
     * assigned to the instance attribute except the first time when it is instantiated.
     * @return DatabaseManager object assigned to the attribute instance
     */
    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }

    /**
     * Method to connect the retrieval request of a list of all beers of the controller with the class in charged of interacting with the
     * database.
     * @return ArrayList of Strings with the names of all the beers in the database
     */
    public ArrayList<String> getBeerList() {
        return databaseTalker.getBeerList();
    }

    /**
     * Method to connect the retrieval request of particularly named beers of the controller with the class in charged of interacting with the
     * database.
     * @param beerName String name of the beer searched
     * @return Arraylist of beers with the provided name
     */
    public ArrayList<Beer> findBeer(String beerName) {
        return databaseTalker.findBeer(beerName);
    }

    /**
     * Method to connect the creation of a beer entry request by the controller with the class in charged of
     * interacting with the database.
     * @param newBeer Beer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the beer was added, false if it was not.
     */
    public boolean addNewBeer(Beer newBeer) {
        return databaseTalker.addNewBeer(newBeer);
    }

    /**
     * Method to connect the update request of a particular beer information by the controller with the class in charged
     * of interacting with the database
     * @param beerToUpdate Beer containing all the information to update its previous version.
     * @return boolean confirming the status of the operation. true if the beer was updated, false if it was not.
     */
    public boolean updateBeer(Beer oldBeer, Beer beerToUpdate) {
        return databaseTalker.updateBeer(oldBeer,beerToUpdate);
    }

    /**
     * Method to connect the retrieval request of a list of all manufacturers of the controller with the class in charged of interacting with the
     * database.
     * @return ArrayList of Strings which correspond to the name of all the manufacturers in the database.
     */
    public ArrayList<String> getManufacturerList() {
        return databaseTalker.getManufacturerList();
    }

    /**
     * Method to connect the retrieval request of particularly named manufacturers of the controller with the class in charged of interacting with the
     * database.
     * @param manufacturerName String of the name of the manufacturer searched in the database
     * @return Manufacturer object that coincide in name with the name provided. It can be empty if no manufacturer has that
     * name.
     */
    public Manufacturer findManufacturer(String manufacturerName) {
        return  databaseTalker.findManufacturer(manufacturerName);
    }

    /**
     * Method to connect the creation of a manufacturer entry request by the controller with the class in charged of
     * interacting with the database.
     * @param newManufacturer Manufacturer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the manufacturer was added, false if it was not.
     */
    public boolean addNewManufacturer(Manufacturer newManufacturer) {
        return databaseTalker.addNewManufacturer(newManufacturer);
    }

    /**
     * Method to connect the update request of a particular manufacturer information by the controller with the class in charged
     * of interacting with the database
     * @param manufacturerToUpdate Manufacturer containing all the information to update its previous version.
     * @return boolean confirming the status of the operation. true if the manufacturer was updated, false if it was not.
     */
    public boolean updateManufacturer(String oldName, Manufacturer manufacturerToUpdate) {
        return  databaseTalker.updateManufacturer(oldName, manufacturerToUpdate);
    }


    /**
     * Method to connect the creation of a user entry request by the controller with the class in charged of
     * interacting with the database.
     * @param username String username that will correspond to the primary key. Must be unique
     * @param password Array of byte the encrypted password
     * @param adminStatus boolean informing whether this is an admin or not
     * @return boolean informing if the insertion has been properly completed
     */
    public boolean createNewUser(String username, byte[] password, boolean adminStatus) {
        return databaseTalker.createNewUser(username,password,adminStatus);
    }

    /**
     * Method to connect the retrieval request of the corresponding encrypted password by the controller with the class in charged
     * of interacting with the database.
     * @param username String username of the corresponding password desired
     * @return Array of bytes of the encrypted password corresponding to the username
     */
    public byte[] getPassword(String username) {
        return databaseTalker.getPassword(username);
    }

    /**
     * Method to connect the retrieval request of the corresponding encrypted password and admin status by the controller with the class in charged
     * of interacting with the database.
     * @param username String username of the corresponding password and status desired
     * @return EncryptedPasswordAndAdminStatus object based on the nested class present in the authentication controller  to encapsulate the encrypted password and the admin status
     */
    public AuthenticationController.EncryptedPasswordAndAdminStatus getPasswordAndAdminStatus(String username) {
        return databaseTalker.getPasswordAndAdminStatus(username);
    }

    /**
     * Method to connect the retrieval request of the corresponding manufacturer name by the controller with the class in charged
     * of interacting with the database.
     * @param username String username of the corresponding manufacturer name desired
     * @return Array list of Strings with the names of the manufacturers this username can perform modifications to and related beers
     */
    public boolean checkManufacturerNameForAuthorization(String username, String manufacturerName) {
        return databaseTalker.checkManufacturerNameForAuthorization(username, manufacturerName);
    }

    /**
     * Method to communicate to the database the insertion of an entry containing the provided parameters by the controller
     * regarding linking a manufacturer and a user
     * @param username String username that is going to be linked to this manufacturer
     * @param manufacturerName String name of the manufacturer that is going to be linked to this user
     * @return String name of the manufacturer this username has access to and derived beers
     */
    public boolean connectManufacturerAndUser(String manufacturerName, String username) {
        return databaseTalker.connectManufacturerAndUser(manufacturerName,username);
    }
}
