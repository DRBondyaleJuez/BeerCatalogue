package com.application.controller;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.persistence.DatabaseManager;
import com.application.web.auxiliary.client.PunkApiClient;
import com.application.web.requests.UpdateBeerInfoRequest;
import com.application.web.requests.UpdateManufacturerInfoRequest;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Provides an object that acts as an intermediary between the web service and the persistence. It performs the
 * actions necessary to fulfill the requests of the web service
 */
public class Controller {

    private final DatabaseManager databaseManager;

    /**
     * This is the constructor. Here  an instance of the DatabaseManager class is assigned to the databaseManager attribute.
     */
    public Controller() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * This method responds to request of retrieving all beers in the data base.
     * @return ArrayList of Strings which correspond to the name of all the beers in the database.
     */
    public ArrayList<String> getBeerList() {
        return databaseManager.getBeerList();
    }

    /**
     * This method responds to the request of retrieving a particular beer with all its information from the database
     * based on a name provided.
     * @param beerName String of the name of the beer searched in the database
     * @return ArrayList of Beer objects that coincide in name with the name provided. It can be empty if no beer has that
     * name.
     */
    public ArrayList<Beer> findBeer(String beerName) {return databaseManager.findBeer(beerName);}

    /**
     * This method responds to the request of retrieving a particular beer with all its information from the alternative
     * source of beer information based on a name provided. It is called when the information is not found in the database.
     * @param beerName String of the name of the beer searched in the alternative beer information source
     * @return ArrayList of Beer objects that coincide in name with the name provided. It can be empty if no beer has that
     * name.
     */
    public ArrayList<Beer> alternativeFindBeer(String beerName) {

        PunkApiClient auxiliaryClient = new PunkApiClient();

        System.out.println("Auxiliary client called");

        return auxiliaryClient.requestBeerData(beerName);
    }

    /**
     * This method responds to the request of creating a new Beer entry in the database based on the information provided.
     * It first checks if the beer is not already in the database and if absent it performs a similar operation with the manufacturer
     * of this new beer in case it also needs creation. Then it assigns the beer and corresponding manufacturer entry a UUID and adds it to the database
     * @param newBeer Beer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the beer was added, false if it was not.
     */
    public boolean addNewBeer(Beer newBeer) {

        //Add the "new" manufacturer
        addNewManufacturer(newBeer.getManufacturer());

        return databaseManager.addNewBeer(newBeer);
    }

    /**
     * This method responds to the request of updating the information of a particular beer entry in the database. First
     * it checks if the update is related to the manufacturer of the beer and acts accordingly. Then checks the beer is
     * in the database and if it is assigns its UUID to the updated beer that will be used to update the beer info in the database.
     * @param updateBeerInfoRequest UpdateBeerInfoRequest object which encapsulates both the requirements for the update,
     *                              this is the old beer and the updated beer.
     * @return boolean confirming the status of the operation. true if the beer was updated, false if it was not.
     */
    public boolean updateBeer(UpdateBeerInfoRequest updateBeerInfoRequest) {

        if(updateBeerInfoRequest == null || updateBeerInfoRequest.getOldBeer()==null || updateBeerInfoRequest.getNewBeer()==null) return false;

        Beer newBeer = updateBeerInfoRequest.getNewBeer();
        Beer oldBeer = updateBeerInfoRequest.getOldBeer();

        return databaseManager.updateBeer(oldBeer,newBeer);
    }

    /**
     * This method responds to request of retrieving all manufacturers in the database.
     * @return ArrayList of Strings which correspond to the name of all the manufacturers in the database.
     */
    public ArrayList<String> getManufacturerList() {
        return databaseManager.getManufacturerList();
    }

    /**
     * This method responds to the request of retrieving a particular manufacturer with all its information from the database
     * based on a name provided.
     * @param manufacturerName String of the name of the manufacturer searched in the database
     * @return Manufacturer object that coincide in name with the name provided. It can be empty if no manufacturer has that
     * name.
     */
    public Manufacturer findManufacturer(String manufacturerName) {
        return databaseManager.findManufacturer(manufacturerName);
    }

    /**
     * This method responds to the request of creating a new Manufacturer entry in the database based on the information provided.
     * It first checks if the manufacturer is not already in the database and if absent it assigns the manufacturer entry a UUID and adds
     * it to the database
     * @param newManufacturer Manufacturer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the manufacturer was added, false if it was not.
     */
    public boolean addNewManufacturer(Manufacturer newManufacturer) {

        if(databaseManager.findManufacturer(newManufacturer.getName()) != null) return false;

        return databaseManager.addNewManufacturer(newManufacturer);
    }

    /**
     * This method responds to the connection of a manufacturer with the username that will be able to edit it and derived beers.
     * @param manufacturerName String the name of the manufacturer which will be connected to the user
     * @param username String name of the user with authorization over this manufacturer
     * @return boolean confirming the status of the operation. true if the manufacturer and user were connected correctly, false if it was not.
     */
    public boolean connectManufacturerAndUser(String manufacturerName, String username) {

        //if(databaseManager.findManufacturer(newManufacturer.getName()) != null ) return false; ///////////////////////// DELETE

        return databaseManager.connectManufacturerAndUser(manufacturerName, username);
    }

    /**
     * This method responds to the request of updating the information of a particular manufacturer entry in the database.
     * First checks the validity and if the manufacturer is in the database. Then if it is assigns its UUID to the updated
     * manufacturer that will be used to update the manufacturer info in the database.
     * @param updateManufacturerInfoRequest UpdateManufacturerInfoRequest object which encapsulates both the requirements for the update,
     *                                      this is the old manufacturer and the updated manufacturer.
     * @return boolean confirming the status of the operation. true if the manufacturer was updated, false if it was not.
     */
    public boolean updateManufacturer(UpdateManufacturerInfoRequest updateManufacturerInfoRequest) {

        if(updateManufacturerInfoRequest == null || updateManufacturerInfoRequest.getNewManufacturer() == null || updateManufacturerInfoRequest.getOldManufacturer() == null) return false;
        Manufacturer newManufacturer = updateManufacturerInfoRequest.getNewManufacturer();
        Manufacturer oldManufacturer = updateManufacturerInfoRequest.getOldManufacturer();

        return databaseManager.updateManufacturer(oldManufacturer.getName(), newManufacturer);
    }

    private boolean checkIfBeerExists(Beer candidateBeer, ArrayList<Beer> beerList){

        if(beerList.isEmpty()) return false;

        for (Beer currentBeer:beerList) {
            if(currentBeer.getManufacturer().getName().equals(candidateBeer.getManufacturer().getName())){
                return true;
            }
        }
        return false;
    }

    private Beer getParticularBeerFromList(Beer candidateBeer, ArrayList<Beer> beerList){

        for (Beer currentBeer:beerList) {
            if(currentBeer.getManufacturer().getName().equals(candidateBeer.getManufacturer().getName())){
                return currentBeer;
            }
        }
        return null;
    }

}
