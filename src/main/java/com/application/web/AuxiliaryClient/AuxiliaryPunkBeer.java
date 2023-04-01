package com.application.web.AuxiliaryClient;

public class AuxiliaryPunkBeer {

    private int id;
    private String name;
    private String tagline;
    private String first_brewed;
    private String description;
    private String image_url;
    private double abv;
    private double ibu;
    private double target_fg;
    private double target_og;
    private double ebc;
    private double srm;
    private double ph;
    private double attenuation_level;



    public AuxiliaryPunkBeer() {
    }

    //GETTERS

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getFirst_brewed() {
        return first_brewed;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public double getAbv() {
        return abv;
    }


    //SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setFirst_brewed(String first_brewed) {
        this.first_brewed = first_brewed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

}
