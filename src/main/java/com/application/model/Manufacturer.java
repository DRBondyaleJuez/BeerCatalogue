package com.application.model;

import java.util.UUID;

public class Manufacturer {

    private String name;
    private String nationality;
    private UUID id;

    public Manufacturer(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
        id = UUID.randomUUID();
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
