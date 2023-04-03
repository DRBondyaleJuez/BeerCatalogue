package com.application.model;

import java.util.UUID;

/**
 * Provides objects that represent beer manufacturing profiles. At least the information necessary for the catalogue.
 */
public class Manufacturer {

    private final String name;
    private final String nationality;

    /**
     * This is the constructor.
     * @param name String name of the manufacturer or brand
     * @param nationality String nationality corresponding to the fiscal country of the company that produces the beer or has the brand
     */
    public Manufacturer(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }
}
