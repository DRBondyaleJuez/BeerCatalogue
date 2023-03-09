package com.application.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


}
