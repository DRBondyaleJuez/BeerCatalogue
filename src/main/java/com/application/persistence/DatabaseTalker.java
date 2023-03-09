package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.ArrayList;

public interface DatabaseTalker {


    ArrayList<String> getBeerList();

    Beer findBeer();

    boolean addNewBeer(Beer newBeer);

    boolean updateBeer(Beer beerToUpdate);

    ArrayList<String> getManufacturerList();

    Manufacturer findManufacturer(String manufacturerName);

    boolean addNewManufacturer(Manufacturer newManufacturer);

    boolean updateManufacturer(Manufacturer manufacturerToUpdate);
}
