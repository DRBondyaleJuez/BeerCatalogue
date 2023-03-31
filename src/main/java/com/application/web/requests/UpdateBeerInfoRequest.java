package com.application.web.requests;

import com.application.model.Beer;

public class UpdateBeerInfoRequest {

    private Beer newBeer;
    private Beer oldBeer;

    public UpdateBeerInfoRequest( Beer oldBeer,Beer newBeer) {
        this.newBeer = newBeer;
        this.oldBeer = oldBeer;
    }

    public Beer getNewBeer() {
        return newBeer;
    }

    public Beer getOldBeer() {
        return oldBeer;
    }
}
