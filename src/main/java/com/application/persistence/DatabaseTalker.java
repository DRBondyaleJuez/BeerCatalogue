package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.ArrayList;
import java.util.UUID;

public interface DatabaseTalker {


    ArrayList<String> getBeerList();

    ArrayList<Beer> findBeer(String beerName);

    boolean addNewBeer(Beer newBeer);

    boolean updateBeer(Beer beerToUpdate);

    ArrayList<String> getManufacturerList();

    Manufacturer findManufacturer(String manufacturerName);

    boolean addNewManufacturer(Manufacturer newManufacturer);

    boolean updateManufacturer(Manufacturer manufacturerToUpdate);

    boolean checkBeerPresent(Beer newBeer);
}
