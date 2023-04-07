package com.application.web;


import com.application.controller.AuthenticationController;
import com.application.controller.AuthorizationController;
import com.application.controller.Controller;
import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.web.auxiliary.client.PunkApiClient;
import com.application.web.requests.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Provides the RestController of the spring framework to handle all endpoints of the service. This class has the
 * @RestController annotation to allow interaction with the Application Class which manages the start point of the
 * service with the @SpringBootApplication annotation
 */
@RestController
public class WebService {

    private final Controller controller;
    private final AuthenticationController authenticationController;
    private final AuthorizationController authorizationController;

    /**
     * This is the constructor where the different controllers are instantiated and assigned to the corresponding attribute.
     */
    public WebService() {

        controller = new Controller();
        authenticationController = new AuthenticationController();
        authorizationController = new AuthorizationController();
    }


    @GetMapping("/index")
    public String getGreetings() {
        return "Hello world";
    }

    //------------Beer Related EndPoints

    /**
     * This method is a put type http method of this endpoint to insert a new user account in the database.
     * @param createNewUserRequest CreateNewUserRequest object with all the information needed in its attributes for the
     *                             account creation.
     * @return ResponseEntity containing a string which consists only on a positive or negative message depending on the
     * the methods success and the corresponding http status.
     */
    @PutMapping("/users")
    public ResponseEntity<String> createNewUser(@RequestBody CreateNewUserRequest createNewUserRequest) {

        addNewManufacturerDuringUserCreation(createNewUserRequest.getManufacturer());

        boolean newUserCreatedCorrectly = authenticationController.createNewUser(createNewUserRequest.getNewUsername(),
                createNewUserRequest.getPassword(),
                createNewUserRequest.isAdminStatus(),
                createNewUserRequest.getManufacturer().getName());

        if(newUserCreatedCorrectly){
            return new ResponseEntity<>("New user created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Unable to create user", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This method is a post http method of this endpoint to sign in, this means, verify credentials are present in the user section of the database i.e. an account has been
     * previously created. It provides the token for continued operation in the database without providing the account details constantly.
     * @param signInRequest SignInRequest object containing the necessary elements to verify credentials.
     * @return UUID representing a token to identify the user without providing all the details repeatedly.
     */
    @PostMapping("/users")
    public ResponseEntity<UUID> signIn(@RequestBody SignInRequest signInRequest) {

        UUID userToken = authenticationController.signIn(signInRequest.getUsername(),
                signInRequest.getPassword());

        if(userToken != null){
            return new ResponseEntity<>(userToken, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

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
     * with the name provided. It searches in the database and if unsuccessful it redirects the name searched to a particular
     * API.
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
        ArrayList<Beer> beerSearched = controller.alternativeFindBeer(beerName);

        if(beerSearched == null || beerSearched.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); /////////////////////////////////////////////// SHOULD THIS BE NOT FOUND?
        } else {
            return new ResponseEntity<>(beerSearched, HttpStatus.TEMPORARY_REDIRECT);
        }
    }

    /**
     * The method corresponding to the POST method of this endpoint to request the creation of a beer entry in the database
     * corresponding to the provided beer if the appropriate token is provided.
     * @param addNewBeerRequest AddNewBeerRequest object containing Beer object containing all the information necessary for the database
     *                          and a token UUID for authentication and authorization.
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PostMapping("/beers")
    public ResponseEntity<String> addNewBeer(@RequestBody AddNewBeerRequest addNewBeerRequest) {

        if(!authenticateAndAuthorize(addNewBeerRequest.getAuthenticationToken(),addNewBeerRequest.getNewBeer().getManufacturer().getName())){
            return new ResponseEntity<>("You are not authorized to perform this operation. Make sure you are using the correct authentication token", HttpStatus.UNAUTHORIZED);
        }

        Beer newBeer = addNewBeerRequest.getNewBeer();

        boolean beerAddedCorrectly = controller.addNewBeer(newBeer);

        if(!beerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new beer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Beer (" + newBeer.getName() + ") has been added correctly to the database.", HttpStatus.CREATED);
        }
    }


    /**
     * The method corresponding to the PUT method of this endpoint to request the update of a beer entry in the database
     * a new updated beer is provided to change the beer info in the database if the appropriate token is provided.
     * @param updateBeerInfoRequest UpdateBeerInfoRequest object which encapsulates the parameters needed for the update.
     *                              The beer already in the database, the updated beer with changes for the update and the authentication token.
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PatchMapping("/beers")
    public ResponseEntity<String> updateBeerInfo(@RequestBody UpdateBeerInfoRequest updateBeerInfoRequest) {

        if(!authenticateAndAuthorize(updateBeerInfoRequest.getAuthenticationToken(),updateBeerInfoRequest.getNewBeer().getManufacturer().getName())){
            return new ResponseEntity<>("You are not authorized to perform this operation. Make sure you are using the correct authentication token", HttpStatus.UNAUTHORIZED);
        }

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
     * corresponding to the provided manufacturer if the appropriate token is provided.
     * @param addNewManufacturerRequest AddNewManufacturerRequest object containing all the information necessary for the database
     *                                  and for authentication
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PostMapping("/manufacturers")
    public ResponseEntity<String> addNewManufacturer(@RequestBody AddNewManufacturerRequest addNewManufacturerRequest) {

        if(!authenticateAndAuthorize(addNewManufacturerRequest.getAuthenticationToken(),addNewManufacturerRequest.getNewManufacturer().getName())){
            return new ResponseEntity<>("You are not authorized to perform this operation. Make sure you are using the correct authentication token", HttpStatus.UNAUTHORIZED);
        }

        Manufacturer newManufacturer = addNewManufacturerRequest.getNewManufacturer();

        boolean manufacturerAddedCorrectly = controller.addNewManufacturer(newManufacturer);

        if(!manufacturerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new manufacturer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + newManufacturer.getName() + ") has been added correctly to the database.", HttpStatus.CREATED);
        }
    }

    /**
     * The method corresponding to the PUT method of this endpoint to request the update of a manufacturer entry in the database
     * a new updated manufacturer is provided to change the manufacturer info in the database if the appropriate token is provided.
     * @param updateManufacturerInfoRequest UpdateManufacturerInfoRequest object which encapsulates the parameters needed for the update.
     *                              The manufacturer already in the database and the updated manufacturer with changes for the update.
     *                                      The authentication token.
     * @return ResponseEntity containing a String of a message and HTTP status both associated with the request performed
     */
    @PatchMapping("/manufacturers")
    public ResponseEntity<String> updateManufacturerInfo(@RequestBody UpdateManufacturerInfoRequest updateManufacturerInfoRequest) {

        if(!authenticateAndAuthorize(updateManufacturerInfoRequest.getAuthenticationToken(),updateManufacturerInfoRequest.getOldManufacturer().getName())){
            return new ResponseEntity<>("You are not authorized to perform this operation. Make sure you are using the correct authentication token", HttpStatus.UNAUTHORIZED);
        }

        boolean manufacturerUpdatedCorrectly = controller.updateManufacturer(updateManufacturerInfoRequest);

        if(!manufacturerUpdatedCorrectly){
            return new ResponseEntity<>("Unable to update manufacturer. Probably it doesn't exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + updateManufacturerInfoRequest.getNewManufacturer().getName() + ") has been updated correctly in the database.", HttpStatus.OK);
        }
    }

    //Add manufacturer only during user account creation
    private ResponseEntity<String> addNewManufacturerDuringUserCreation(@RequestBody Manufacturer newManufacturer) {

        boolean manufacturerAddedCorrectly = controller.addNewManufacturer(newManufacturer);

        if(!manufacturerAddedCorrectly){
            return new ResponseEntity<>("Unable to add new manufacturer. Probably it already exists in the database", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("Manufacturer (" + newManufacturer.getName() + ") has been added correctly to the database.", HttpStatus.OK);
        }
    }

    private boolean authenticateAndAuthorize(UUID authenticationToken, String manufacturerName) {
        if(authenticationToken == null || manufacturerName == null){
            System.out.println("Authentication Token null. Unexpected situation."); ////////////////////////////////////////////////////////////////////////////DELETE
            return false;
        }

        String authenticatedUser = authenticationController.tokenAuthentication(authenticationToken);
        boolean userIsAuthorized = authorizationController.checkAuthorization(authenticatedUser,manufacturerName);

        return authenticatedUser != null && userIsAuthorized;
    }

}
