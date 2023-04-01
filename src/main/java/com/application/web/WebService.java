package com.application.web;


import com.application.controller.Controller;
import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.web.auxiliary.client.PunkApiClient;
import com.application.web.requests.UpdateBeerInfoRequest;
import com.application.web.requests.UpdateManufacturerInfoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Provides the RestController of the spring framework to handle all endpoints of the service. This class has the
 * @RestController annotation to allow interaction with the Application Class which manages the start point of the
 * service with the @SpringBootApplication annotation
 */
@RestController
public class WebService {

    private final Controller controller;

    /**
     * This is the constructor where the controller is instantiated and assigned to the controller attribute.
     */
    public WebService() {
        controller = new Controller();
    }


    @GetMapping("/index")
    public String getGreetings() {
        return "Hello world";
    }

    //------------Beer Related EndPoints

    /**
     * The method corresponding to the GET method of this endpoint to request the retrieval of all the beers in the database.
     * @return ResponseEntity containing the ArrayList of Strings with all the names of beers in the database and the
     * corresponding HTTP status associated to the request performed
     */
    @GetMapping("/allbeers")
    public ResponseEntity<ArrayList<String>> getBeerList() {

        ArrayList<String> beerList = controller.getBeerList();

        if(beerList.isEmpty()){
            return new ResponseEntity<>(beerList, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(beerList, HttpStatus.OK);
        }
    }

    /**
     * The method corresponding to the GET method of this endpoint to request the retrieval o all the information of beer
     * with the name provided
     * @param beerName String of the name of the beer being searched
     * @return ResponseEntity containing the ArrayList of Beers in the database that coincide with the name provided and the
     *         corresponding HTTP status associated to the request performed
     */
    @GetMapping("/beers")
    public ResponseEntity<ArrayList<Beer>> getBeerDetails(@RequestParam String beerName) {
        ArrayList<Beer> beerSearched = controller.findBeer(beerName);

        if(beerSearched == null || beerSearched.isEmpty()){
            return alternativeBeerSearch(beerName);
        } else {
            return new ResponseEntity<>(beerSearched, HttpStatus.OK);
        }
    }

    //If the beer is not found in the database it can still try and find it in the punkapi acting as a client
    private ResponseEntity<ArrayList<Beer>> alternativeBeerSearch(String beerName){
        PunkApiClient auxiliaryClient = new PunkApiClient();

        System.out.println("Auxiliary client called");
        ArrayList<Beer> beerSearched = auxiliaryClient.requestBeerData(beerName);

        if(beerSearched == null || beerSearched.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); /////////////////////////////////////////////// SHOULD THIS BE NOT FOUND?
        } else {
            return new ResponseEntity<>(beerSearched, HttpStatus.TEMPORARY_REDIRECT);
        }


    }

    /**
     * The method corresponding to the POST method of this endpoint to request the creation of a beer entry in the database
     * corresponding to the provided beer.
     * @param newBeer Beer object containing all the information necessary for the database
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PostMapping("/beers")
    public ResponseEntity<String> addNewBeer(@RequestBody Beer newBeer) {
        boolean beerAddedCorrectly = controller.addNewBeer(newBeer);

        if(!beerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new beer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + newBeer.getName() + ") has been added correctly to the database.", HttpStatus.OK);
        }
    }

    /**
     * The method corresponding to the PUT method of this endpoint to request the update of a beer entry in the database
     * a new updated beer is provided to change the beer info in the database.
     * @param updateBeerInfoRequest UpdateBeerInfoRequest object which encapsulates the parameters needed for the update.
     *                              The beer already in the database and the ne update beer with changes for the update.
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PutMapping("/beers")
    public ResponseEntity<String> updateBeerInfo(@RequestBody UpdateBeerInfoRequest updateBeerInfoRequest) {
        boolean beerUpdatedCorrectly = controller.updateBeer(updateBeerInfoRequest);

        if(!beerUpdatedCorrectly){
            return new ResponseEntity<>("Unable to update beer. Probably it doesn't exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + updateBeerInfoRequest.getNewBeer().getName() + ") has been updated correctly in the database.", HttpStatus.OK);
        }
    }


    //------------Manufacturer Related EndPoints

    /**
     * The method corresponding to the GET method of this endpoint to request the retrieval of all the manufacturers in the database.
     * @return ResponseEntity containing the ArrayList of Strings with all the names of manufacturers in the database and the
     * corresponding HTTP status associated to the request performed
     */
    @GetMapping("/allmanufacturers")
    public ResponseEntity<ArrayList<String>>  getManufacturerList() {

        ArrayList<String> manufacturerList = controller.getManufacturerList();

        if(manufacturerList.isEmpty()){
            return new ResponseEntity<>(manufacturerList, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(manufacturerList, HttpStatus.OK);
        }
    }

    /**
     * The method corresponding to the GET method of this endpoint to request the retrieval o all the information of manufacturer
     * with the name provided
     * @param manufacturerName String of the name of the manufacturer being searched
     * @return ResponseEntity containing the ArrayList of Manufacturer objects in the database that coincide with the name provided and the
     *         corresponding HTTP status associated to the request performed
     */
    @GetMapping("/manufacturers")
    public ResponseEntity<Manufacturer>  getManufacturerDetail(@RequestParam String manufacturerName) {
        Manufacturer manufacturerSearched = controller.findManufacturer(manufacturerName);

        if(manufacturerSearched == null){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(manufacturerSearched, HttpStatus.OK);
        }
    }

    /**
     * The method corresponding to the POST method of this endpoint to request the creation of a manufacturer entry in the database
     * corresponding to the provided manufacturer.
     * @param newManufacturer Manufacturer object containing all the information necessary for the database
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PostMapping("/manufacturers")
    public ResponseEntity<String> addNewManufacturer(@RequestBody Manufacturer newManufacturer) {
        boolean manufacturerAddedCorrectly = controller.addNewManufacturer(newManufacturer);

        if(!manufacturerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new manufacturer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + newManufacturer.getName() + ") has been added correctly to the database.", HttpStatus.OK);
        }
    }

    /**
     * The method corresponding to the PUT method of this endpoint to request the update of a manufacturer entry in the database
     * a new updated manufacturer is provided to change the manufacturer info in the database.
     * @param updateManufacturerInfoRequest UpdateManufacturerInfoRequest object which encapsulates the parameters needed for the update.
     *                              The manufacturer already in the database and the updated manufacturer with changes for the update.
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PutMapping("/manufacturers")
    public ResponseEntity<String> updateManufacturerInfo(@RequestBody UpdateManufacturerInfoRequest updateManufacturerInfoRequest) {
        boolean manufacturerUpdatedCorrectly = controller.updateManufacturer(updateManufacturerInfoRequest);

        if(!manufacturerUpdatedCorrectly){
            return new ResponseEntity<>("Unable to update manufacturer. Probably it doesn't exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + updateManufacturerInfoRequest.getNewManufacturer().getName() + ") has been updated correctly in the database.", HttpStatus.OK);
        }
    }


}
