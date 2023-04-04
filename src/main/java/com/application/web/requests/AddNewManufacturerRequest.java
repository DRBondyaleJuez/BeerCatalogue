package com.application.web.requests;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.UUID;

public class AddNewManufacturerRequest {
    Manufacturer newManufacturer;
    UUID authenticationToken;

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
