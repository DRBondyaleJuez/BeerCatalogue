package com.application.web.requests;

import com.application.model.Beer;

/**
 * Provides an encapsulation of the object necessary to perform the update of the beer information through a put action
 * in the webService. This object will be required in the body of the request.
 */
public class UpdateBeerInfoRequest {

    private Beer newBeer;
    private Beer oldBeer;

    /**
     * This is the constructor with all the parameters needed for the put request
     * @param oldBeer Beer object containing the information of the beer all ready in the database
     * @param newBeer Beer object containing the new information of the beer
     */
    public UpdateBeerInfoRequest( Beer oldBeer,Beer newBeer) {
        this.newBeer = newBeer;
        this.oldBeer = oldBeer;
    }

    //GETTERS
    public Beer getNewBeer() {
        return newBeer;
    }

    public Beer getOldBeer() {
        return oldBeer;
    }
}
