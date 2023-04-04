package com.application.web.requests;

import com.application.model.Beer;

import java.util.UUID;

/**
 * Provides an encapsulation of the object necessary to perform the insertion of the beer information through a post action
 * in the webService. This object will be required in the body of the request.
 */
public class AddNewBeerRequest {
    Beer newBeer;
    UUID authenticationToken;

    /**
     * This is the constructor with all the parameters needed for the post request
     * @param newBeer Beer object containing the information of the beer that will be inserted
     * @param authenticationToken UUID object corresponding to the token required to perform the authentication
     */
    public AddNewBeerRequest(Beer newBeer, UUID authenticationToken) {
        this.newBeer = newBeer;
        this.authenticationToken = authenticationToken;
    }

    //GETTERS
    public Beer getNewBeer() {
        return newBeer;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }
}
