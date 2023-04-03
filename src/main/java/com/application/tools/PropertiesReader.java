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

    private static final String  urlSource = "/secrets.properties2";

    /**
     * Static method to collect and use the password of the database application from the secrets.properties file
     * @return String containing the content in the corresponding space designated for the password. If this space has been
     * deleted or it is not found it returns "".
     */
    public static String getDBPassword(){
        URL secretsURL = PropertiesReader.class.getResource(urlSource);
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

    public static String getDBUser() {

        URL secretsURL = PropertiesReader.class.getResource(urlSource);
        String secretDBUser;
        try {
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("DBUser")){
                    secretDBUser = currentString.replace("DBUser :: ","");
                    return secretDBUser;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static String getEncryptionKey() {

        URL secretsURL = PropertiesReader.class.getResource(urlSource);
        String secretEncryptionKey;
        try {
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("encryptionKey")){
                    secretEncryptionKey = currentString.replace("encryptionKey :: ","");
                    return secretEncryptionKey;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static int getSaltSize() {

        URL secretsURL = PropertiesReader.class.getResource(urlSource);
        int secretSaltSize;
        try {
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("saltSize")){
                    secretSaltSize = Integer.parseInt(currentString.replace("saltSize :: ",""));
                    return secretSaltSize;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static int getInitialSubstringPositionForTransposition() {

        URL secretsURL = PropertiesReader.class.getResource(urlSource);
        int secretInitialSubstringPositionForTransposition;
        try {
            BufferedReader secretsReader = new BufferedReader(new FileReader(String.valueOf(secretsURL).replace("file:/","")));
            String currentString;

            while ((currentString = secretsReader.readLine()) != null) {

                if(currentString.contains("initialSubstringPositionForTransposition")){
                    secretInitialSubstringPositionForTransposition = Integer.parseInt(currentString.replace("initialSubstringPositionForTransposition :: ",""));
                    return secretInitialSubstringPositionForTransposition;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
