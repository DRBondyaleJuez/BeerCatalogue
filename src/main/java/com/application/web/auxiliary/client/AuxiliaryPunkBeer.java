package com.application.web.auxiliary.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Provides the BEAN object to serialize the JSON data retrieved from the punkApi when the beer is not found in the database.
 */

@JsonIgnoreProperties(ignoreUnknown = true) //TODO: Test if the other thing is unnecessary
public class AuxiliaryPunkBeer {

    private String name;
    private String tagline;

    private String description;
    private double abv;


    /**
     * This is the empty constructor. Setters need to be used.
     */
    public AuxiliaryPunkBeer() {
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public double getAbv() {
        return abv;
    }


    //SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setAbv(double abv) {
        this.abv = abv;
    }

}
