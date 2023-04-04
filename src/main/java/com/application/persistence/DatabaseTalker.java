package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Provides the abstract methods necessary for a proper use by calls from the DatabaseManager Class basically for performing
 * the appropriate operations in the database. The class which implements this interface will need to implement the methods.
 */
public interface DatabaseTalker {


    /**
     * Build and submit the SQL statement to command the retrieval of all beers ordered by name from the database
     * @return ArrayList of Strings which correspond to the name of all the beers in the database sorted alphabetically.
     */
    ArrayList<String> getBeerList();

    /**
     * Build and submit the SQL statement to command the retrieval of the particular beers with provided name.
     * @param beerName String of the name of the beer searched in the database
     * @return ArrayList of Beer objects that coincide in name with the name provided. It can be empty if no beer has that
     * name.
     */
    ArrayList<Beer> findBeer(String beerName);

    /**
     * Build and submit the SQL statement to command the creation of the beer entry in the database based on the provided beer info.
     * @param newBeer Beer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the beer was added, false if it was not.
     */
    boolean addNewBeer(Beer newBeer);

    /**
     * Build and submit the SQL statement to command the update of the beer entry in the database based on the provided beer info.
     * @param updatedBeer Beer containing all the information to update its previous version.
     * @return boolean confirming the status of the operation. true if the beer was updated, false if it was not.
     */
    boolean updateBeer(Beer oldBeer, Beer updatedBeer);

    /**
     * Build and submit the SQL statement to command the retrieval of all manufacturers sorted by name from the database
     * @return ArrayList of Strings which correspond to the name of all the manufacturers in the database.
     */
    ArrayList<String> getManufacturerList();

    /**
     * Build and submit the SQL statement to command the retrieval of the particular manifacturer with provided name.
     * @param manufacturerName String of the name of the manufacturer searched in the database
     * @return Manufacturer object that coincide in name with the name provided. It can be empty if no manufacturer has that
     * name.
     */
    Manufacturer findManufacturer(String manufacturerName);

    /**
     * Build and submit the SQL statement to command the creation of the manufacturer entry in the database based on the provided manufacturer info.
     * @param newManufacturer Manufacturer object containing all the information required for the entry
     * @return boolean confirming the status of the operation. true if the manufacturer was added, false if it was not.
     */
    boolean addNewManufacturer(Manufacturer newManufacturer);

    /**
     * Build and submit the SQL statement to command the update of the manufacturer entry in the database based on the provided manufacturer info.
     * @param updatedManufacturer Manufacturer containing all the information to update its previous version.
     * @return boolean confirming the status of the operation. true if the manufacturer was updated, false if it was not.
     */
    boolean updateManufacturer(String OldName, Manufacturer updatedManufacturer);

    boolean createNewUser(String username, byte[] password, boolean adminStatus, String manufacturerName);

    byte[] getPassword(String username);

    String checkManufacturerNameForAuthorization(String username);
}
