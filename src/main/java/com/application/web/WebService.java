package com.application.web;


import com.application.controller.Controller;
import com.application.model.Beer;
import com.application.model.Manufacturer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * This class acts as a rest controller, this means it is the class defining and
 * organizing the endpoints of the service.
 */
@RestController
public class WebService {

    private final Controller controller;

    public WebService() {
        controller = new Controller();
    }


    @GetMapping("/index")
    public String getGreetings() {
        return "Hello world";
    }

    //------------Beer Related EndPoints

    @GetMapping("/allbeers")
    public ResponseEntity<ArrayList<String>> getBeerList() {

        ArrayList<String> beerList = controller.getBeerList();

        if(beerList.isEmpty()){
            return new ResponseEntity<>(beerList, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(beerList, HttpStatus.OK);
        }
    }

    @GetMapping("/beers")
    public ResponseEntity<ArrayList<Beer>> getBeerDetails(@RequestParam String beerName) {
        ArrayList<Beer> beerSearched = controller.findBeer(beerName);

        if(beerSearched == null){
            return new ResponseEntity<>(beerSearched, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(beerSearched, HttpStatus.OK);
        }
    }

    @PostMapping("/beers")
    public ResponseEntity<String> addNewBeer(@RequestBody Beer newBeer) {
        boolean beerAddedCorrectly = controller.addNewBeer(newBeer);

        if(!beerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new beer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + newBeer.getName() + ") has been added correctly to the database.", HttpStatus.OK);
        }
    }

    @PutMapping("/beers")
    public ResponseEntity<String> updateBeerInfo(@RequestBody Beer beerToUpdate) {
        boolean beerUpdatedCorrectly = controller.updateBeer(beerToUpdate);

        if(!beerUpdatedCorrectly){
            return new ResponseEntity<>("Unable to update beer. Probably it doesn't exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + beerToUpdate.getName() + ") has been updated correctly in the database.", HttpStatus.OK);
        }
    }


    //------------Manufacturer Related EndPoints

    @GetMapping("/allmanufacturers")
    public ResponseEntity<ArrayList<String>>  getManufacturerList() {

        ArrayList<String> manufacturerList = controller.getManufacturerList();

        if(manufacturerList.isEmpty()){
            return new ResponseEntity<>(manufacturerList, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(manufacturerList, HttpStatus.OK);
        }
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<Manufacturer>  getManufacturerDetail(@RequestParam String manufacturerName) {
        Manufacturer manufacturerSearched = controller.findManufacturer(manufacturerName);

        if(manufacturerSearched == null){
            return new ResponseEntity<>(manufacturerSearched, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(manufacturerSearched, HttpStatus.OK);
        }
    }

    @PostMapping("/manufacturers")
    public ResponseEntity<String> addNewManufacturer(@RequestBody Manufacturer newManufacturer) {
        boolean manufacturerAddedCorrectly = controller.addNewManufacturer(newManufacturer);

        if(!manufacturerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new beer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + newManufacturer.getName() + ") has been added correctly to the database.", HttpStatus.OK);
        }
    }

    @PutMapping("/manufacturers")
    public ResponseEntity<String> updateManufacturerInfo(@RequestBody Manufacturer manufacturerToUpdate) {
        boolean beerUpdatedCorrectly = controller.updateManufacturer(manufacturerToUpdate);

        if(!beerUpdatedCorrectly){
            return new ResponseEntity<>("Unable to update manufacturer. Probably it doesn't exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + manufacturerToUpdate.getName() + ") has been updated correctly in the database.", HttpStatus.OK);
        }
    }


}
