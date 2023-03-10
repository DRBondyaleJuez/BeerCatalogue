package com.application.controller;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.persistence.DatabaseManager;

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

    public boolean updateBeer(Beer beerToUpdate) {
        return databaseManager.updateBeer(beerToUpdate);
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

    public boolean updateManufacturer(Manufacturer manufacturerToUpdate) {
        return databaseManager.updateManufacturer(manufacturerToUpdate);
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

}
