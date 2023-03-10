package com.application.controller;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.persistence.DatabaseManager;
import com.application.web.requests.UpdateBeerInfoRequest;
import com.application.web.requests.UpdateManufacturerInfoRequest;

import java.util.ArrayList;
import java.util.UUID;

public class Controller {

    private DatabaseManager databaseManager;

    public Controller() {

        databaseManager = DatabaseManager.getInstance();

    }

    public ArrayList<String> getBeerList() {
        return databaseManager.getBeerList();
    }

    public ArrayList<Beer> findBeer(String beerName) {
        return databaseManager.findBeer(beerName);
    }

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

    public ArrayList<String> getManufacturerList() {
        return databaseManager.getManufacturerList();
    }

    public Manufacturer findManufacturer(String manufacturerName) {
        return databaseManager.findManufacturer(manufacturerName);
    }

    public boolean addNewManufacturer(Manufacturer newManufacturer) {
        return databaseManager.addNewManufacturer(newManufacturer);
    }

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
