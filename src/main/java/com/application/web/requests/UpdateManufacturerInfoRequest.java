package com.application.web.requests;

import com.application.model.Manufacturer;

import java.util.UUID;

/**
 * Provides an object representing the encapsulation of the arguments necessary to perform the update of the manufacturer
 * information through a put action in the webservice. This object will be required in the body of the request.
 */
public class UpdateManufacturerInfoRequest {

    private Manufacturer oldManufacturer;
    private Manufacturer newManufacturer;

    private UUID authenticationToken;

    /**
     * This is the constructor with all the parameters needed for the put request
     * @param oldManufacturer Manufacturer object containing the information of the beer all ready in the database
     * @param newManufacturer Manufacturer object containing the new information of the beer
     */
    public UpdateManufacturerInfoRequest(Manufacturer oldManufacturer, Manufacturer newManufacturer, UUID authenticationToken) {
        this.oldManufacturer = oldManufacturer;
        this.newManufacturer = newManufacturer;
        this.authenticationToken = authenticationToken;
    }

    //GETTERS
    public Manufacturer getOldManufacturer() {
        return oldManufacturer;
    }

    public Manufacturer getNewManufacturer() {
        return newManufacturer;
    }
    public UUID getAuthenticationToken() {
        return authenticationToken;
    }

}

