package com.application.model;

import java.util.UUID;

/**
 * Provides objects that represent beer profile. At least the information necessary for the catalogue
 */
public class Beer {

    private String name;
    private Double graduation;
    private String type;
    private String description;
    private Manufacturer manufacturer;
    private UUID id;

    /**
     * This is the constructor.
     * @param name String the commercial name of the beer
     * @param graduation Double the graduation aka alcohol percentage of the beer
     * @param type String the type of beer
     * @param description String a short description of the beer
     * @param manufacturer Manufacturer object that corresponds to the beer
     */
    public Beer(String name, Double graduation, String type, String description, Manufacturer manufacturer) {
        this.name = name;
        this.graduation = graduation;
        this.type = type;
        this.description = description;
        this.manufacturer = manufacturer;
    }

    /**
     * Setter for the id attribute not declared during construction.
     * @param id UUID unique identification for each beer. It is set after construction
     */
    public void setId(UUID id) {
        this.id = id;
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public Double getGraduation() {
        return graduation;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public UUID getId() {
        return id;
    }
}
