package com.application.persistence;

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
     * @param beerName String
     * @return
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

    public boolean createNewUser(String username, byte[] password, boolean adminStatus, String manufacturerName) {
        return databaseTalker.createNewUser(username,password,adminStatus,manufacturerName);
    }
}
