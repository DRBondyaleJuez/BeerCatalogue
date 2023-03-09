package com.application.web;


import org.springframework.web.bind.annotation.*;

/**
 * This class acts as a rest controller, this means it is the class defining and
 * organizing the endpoints of the service.
 */
@RestController
public class WebService {

    public WebService() {
    }


    @GetMapping("/index")
    public String getGreetings() {
        return "Hello world";
    }

    @GetMapping("/allbeers")
    public String getBeerList() {
        return "Hello world";
    }

    @GetMapping("/beers")
    public String getBeerDetail() {
        return "Hello world";
    }

    @PostMapping("/beers")
    public String addNewBeer() {
        return "Hello world";
    }

    @PutMapping("/beers")
    public String updateBeerInfo() {
        return "Hello world";
    }

    @GetMapping("/allmanufacturers")
    public String getManufacturerList() {
        return "Hello world";
    }

    @GetMapping("/manufacturers")
    public String getManufacturerDetail() {
        return "Hello world";
    }

    @PostMapping("/manufacturers")
    public String addNewManufacturer() {
        return "Hello world";
    }

    @PutMapping("/manufacturers")
    public String updateManufacturerInfo() {
        return "Hello world";
    }


}
