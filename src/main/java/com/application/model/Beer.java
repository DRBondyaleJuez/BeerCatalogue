package com.application.model;

import jdk.jfr.Percentage;

public class Beer {

    private String name;
    private Percentage graduation;
    private String type;
    private String description;
    private String manufacturer;

    public Beer(String name, Percentage graduation, String type, String description, String manufacturer) {
        this.name = name;
        this.graduation = graduation;
        this.type = type;
        this.description = description;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public Percentage getGraduation() {
        return graduation;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
