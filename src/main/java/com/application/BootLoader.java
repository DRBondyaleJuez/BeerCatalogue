package com.application;

import com.application.utils.PropertiesReader;

public class BootLoader {

    public static void bootApplicationProperties(){
        PropertiesReader.loadAllProperties();
    }


}
