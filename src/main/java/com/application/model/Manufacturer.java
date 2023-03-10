package com.application.model;

import java.util.UUID;

public class Manufacturer {

    private final String name;
    private final String nationality;
    private UUID id;

    public Manufacturer(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
        id = null;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public UUID getId() {
        return id;
    }
}
