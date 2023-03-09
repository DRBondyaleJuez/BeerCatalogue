package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.ArrayList;

public class DatabaseManager {

    private final DatabaseTalker databaseTalker;
    private static DatabaseManager instance;

    private DatabaseManager(){
        databaseTalker = new PostgreSQLDBConnection("BeerCatalogue");
        instance = null;
    }

    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }


    public ArrayList<String> getBeerList() {
        return databaseTalker.getBeerList();
    }

    public Beer findBeer() {
        return databaseTalker.findBeer();
    }

    public boolean addNewBeer(Beer newBeer) {
        return databaseTalker.addNewBeer(newBeer);
    }

    public boolean updateBeer(Beer beerToUpdate) {
        return databaseTalker.updateBeer(beerToUpdate);
    }

    public ArrayList<String> getManufacturerList() {
        return databaseTalker.getManufacturerList();
    }

    public Manufacturer findManufacturer(String manufacturerName) {
        return  databaseTalker.findManufacturer(manufacturerName);
    }

    public boolean addNewManufacturer(Manufacturer newManufacturer) {
        return databaseTalker.addNewManufacturer(newManufacturer);
    }

    public boolean updateManufacturer(Manufacturer manufacturerToUpdate) {
        return  databaseTalker.updateManufacturer(manufacturerToUpdate);
    }
}
