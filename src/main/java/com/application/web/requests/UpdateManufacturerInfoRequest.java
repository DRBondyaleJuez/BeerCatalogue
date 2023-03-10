package com.application.web.requests;

import com.application.model.Manufacturer;

public class UpdateManufacturerInfoRequest {

    private Manufacturer oldManufacturer;
    private Manufacturer newManufacturer;

    public UpdateManufacturerInfoRequest(Manufacturer oldManufacturer, Manufacturer newManufacturer) {
        this.oldManufacturer = oldManufacturer;
        this.newManufacturer = newManufacturer;
    }

    public Manufacturer getOldManufacturer() {
        return oldManufacturer;
    }

    public Manufacturer getNewManufacturer() {
        return newManufacturer;
    }
}
