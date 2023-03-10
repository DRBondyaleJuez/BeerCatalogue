package com.application.model;

import jdk.jfr.Percentage;

import java.util.UUID;

public class Beer {

    private String name;
    private Double graduation;
    private String type;
    private String description;
    private Manufacturer manufacturer;
    private UUID id;

    public Beer(String name, Double graduation, String type, String description, Manufacturer manufacturer) {
        this.name = name;
        this.graduation = graduation;
        this.type = type;
        this.description = description;
        this.manufacturer = manufacturer;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
