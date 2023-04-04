package com.application.web.requests;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.UUID;

/**
 * Provides an object representing the encapsulation of the arguments necessary to perform the insertion of the manufacturer
 * information through a post action in the webservice. This object will be required in the body of the request.
 */
public class AddNewManufacturerRequest {
    Manufacturer newManufacturer;
    UUID authenticationToken;

    /**
     * This is the constructor with all the parameters needed for the post request
     * @param newManufacturer Manufacturer object containing the information of the beer that will be inserted
     * @param authenticationToken UUID object corresponding to the token required to perform the authentication
     */
    public AddNewManufacturerRequest(Manufacturer newManufacturer, UUID authenticationToken) {
        this.newManufacturer = newManufacturer;
        this.authenticationToken = authenticationToken;
    }

    public Manufacturer getNewManufacturer() {
        return newManufacturer;
    }

    public UUID getAuthenticationToken() {
        return authenticationToken;
    }
}
