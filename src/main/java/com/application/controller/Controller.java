package com.application.controller;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.persistence.DatabaseManager;

import java.util.ArrayList;

public class Controller {

    private DatabaseManager databaseManager;

    public Controller() {

        databaseManager = DatabaseManager.getInstance();

    }

    public ArrayList<String> getBeerList() {
        return databaseManager.getBeerList();
    }

    public Beer findBeer(String beerName) {
        return databaseManager.findBeer();
    }

    public boolean addNewBeer(Beer newBeer) {
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
}
