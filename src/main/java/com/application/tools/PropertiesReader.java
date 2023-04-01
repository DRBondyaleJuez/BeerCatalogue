package com.application.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * Provides of an intermediary to interpret and use the secretr.properties file
 */
public class PropertiesReader {

    /**
     * Static method to collect and use the password of the database application from the secrets.properties file
     * @return String containing the content in the corresponding space designated for the password. If this space has been
     * deleted or it is not found it returns "".
     */
    public static String getDBPassword(){
        URL secretsURL = PropertiesReader.class.getResource("/secrets.properties");
        String secretDBPassword;
        try {
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("DBPassword")){
                    secretDBPassword = currentString.replace("DBPassword :: ","");
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
