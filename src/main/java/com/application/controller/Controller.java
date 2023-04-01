package com.application.controller;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.persistence.DatabaseManager;
import com.application.web.requests.UpdateBeerInfoRequest;
import com.application.web.requests.UpdateManufacturerInfoRequest;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Provides an object that acts as an intermediary between the web service and the persistence. It performs the
 * actions necessary to fulfill the requests of the web service
 */
public class Controller {

    private DatabaseManager databaseManager;

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
    public ArrayList<Beer> findBeer(String beerName) {
        return databaseManager.findBeer(beerName);
    }

    /**
     * This method responds to the request of creating a new Beer entry in the database based on the information provided.
     * It first checks if the beer is not already in the database and if absent it performs a similar operation with the manufacturer
     * of this new beer in case it also needs creation. Then it assigns the beer and corresponding manufacturer entry a UUID and adds it to the database
     * @param newBeer Beer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the beer was added, false if it was not.
     */
    public boolean addNewBeer(Beer newBeer) {

        //Check if beer already exists
        //boolean beerPresent = databaseManager.checkBeerPresent(newBeer);
        //ArrayList<Beer> checkIfExistsBeer = databaseManager.findBeer(newBeer.getName());
        boolean beerPresentAlready = checkIfBeerExists(newBeer,databaseManager.findBeer(newBeer.getName()));
        if(beerPresentAlready) return false;

        //Add the "new" manufacturer
        addNewManufacturer(newBeer.getManufacturer());

        //Create manufacturer and beer id
        UUID manufacturerId = findManufacturer(newBeer.getManufacturer().getName()).getId();
        UUID beerId = UUID.randomUUID();
        newBeer.setId(beerId);
        newBeer.getManufacturer().setId(manufacturerId);

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

        Beer newBeer = updateBeerInfoRequest.getNewBeer();
        Beer oldBeer = updateBeerInfoRequest.getOldBeer();

        //In case the update affected the manufacturer
        UpdateManufacturerInfoRequest preventiveUpdateManufacturerInfoRequest = new UpdateManufacturerInfoRequest(oldBeer.getManufacturer(), newBeer.getManufacturer());
        boolean isManufacturerUpdated = updateManufacturer(preventiveUpdateManufacturerInfoRequest);
        if(!isManufacturerUpdated) return false;

        //Once the manufacturer if needed has been modified the id of the current manufacturer needs to be added to the manufacturer of the updatedBeer i.e. the newBeer
        Manufacturer manufacturerInDatabase = findManufacturer(newBeer.getManufacturer().getName());
        if(manufacturerInDatabase == null) return false;

        //Then the same is necessary to retrieve the id of the beer we are going to update based on the oldBeer
        ArrayList<Beer> beerInDatabaseList = findBeer(oldBeer.getName());
        if(beerInDatabaseList == null || beerInDatabaseList.isEmpty()) return false;
        Beer beerInDatabase = getParticularBeerFromList(oldBeer,beerInDatabaseList);
        if(beerInDatabase == null) return false;

        UUID beerId = beerInDatabase.getId();
        newBeer.setId(beerId);

        return databaseManager.updateBeer(newBeer);
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

        UUID manufacturerId = UUID.randomUUID();
        newManufacturer.setId(manufacturerId);

        return databaseManager.addNewManufacturer(newManufacturer);
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

        Manufacturer newManufacturer = updateManufacturerInfoRequest.getNewManufacturer();
        Manufacturer oldManufacturer = updateManufacturerInfoRequest.getOldManufacturer();

        if(newManufacturer == null || oldManufacturer == null) return false;

        Manufacturer manufacturerInDatabase = findManufacturer(oldManufacturer.getName());
        if(manufacturerInDatabase == null) return false;

        UUID manufacturerId = manufacturerInDatabase.getId();
        newManufacturer.setId(manufacturerId);

        return databaseManager.updateManufacturer(newManufacturer);
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
