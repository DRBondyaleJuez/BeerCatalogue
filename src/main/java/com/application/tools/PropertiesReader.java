package com.application.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class PropertiesReader {

    public static String getDBPassword(){
        URL secretsURL = PropertiesReader.class.getResource("/secrets.properties");
        String secretDBPassword;
        try {
            System.out.println(String.valueOf(secretsURL).replace("file:/",""));
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("DBPassword")){
                    secretDBPassword = currentString.replace("DBPassword :: ","");
                    System.out.println("String with password: " + secretDBPassword);
                    return secretDBPassword;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

}
