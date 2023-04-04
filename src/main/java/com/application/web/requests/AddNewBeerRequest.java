package com.application.web.requests;

import com.application.model.Beer;

import java.util.UUID;

public class AddNewBeerRequest {
    Beer newBeer;
    UUID authenticationToken;

    public AddNewBeerRequest(Beer newBeer, UUID authenticationToken) {
        this.newBeer = newBeer;
        this.authenticationToken = authenticationToken;
    }

    public Beer getNewBeer() {
        return newBeer;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }
}
