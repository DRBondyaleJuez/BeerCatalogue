package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.tools.PropertiesReader;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Provides object of the class in charged of the interaction with the database. In this case a PostgreSQL database. This
 * class implements DatabaseTalker the interface providing the template for the abstract methods that require implementation
 * to fulfill this application's requirements.
 */
public class PostgreSQLDBConnection implements DatabaseTalker{

    private String database;
    private final String url;
    private final String user = "postgres";
    private final String password;
    private final Connection currentConnection;

    /**
     * This is the constructor which requires the name of the database to be instantiated
     * @param currentDatabase String name of the database
     */
    public PostgreSQLDBConnection(String currentDatabase) {

        database = currentDatabase;
        url = "jdbc:postgresql://localhost/" + database;
        password = PropertiesReader.getDBPassword();
        currentConnection = connect();
    }

    /**
     * Connect to the PostgreSQL database
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE: " + e.getMessage());
        }

        return conn;
    }

    //GETTER
    public Connection getConnection() {
        return currentConnection;
    }


    @Override
    public ArrayList<String> getBeerList() {

        ArrayList<String> beerList = new ArrayList<>();

        String sql = "SELECT name " +
                "FROM beers " +
                "ORDER BY name";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the purchase info retrieval: "+ resultSet);
            while (resultSet.next()) {
                String currentBeer = resultSet.getString("name");
                beerList.add(currentBeer);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return beerList;
    }

    @Override
    public ArrayList<Beer> findBeer(String beerName) {
        ArrayList<Beer> foundBeers = new ArrayList<>();

        String sql = "SELECT beer_id,beers.name AS beer_name,graduation,type,description,manufacturers.name AS manufacturer_name,nationality " +
                "FROM beers " +
                "INNER JOIN manufacturers USING (manufacturer_id) " +
                "WHERE beers.name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, beerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from beerFinder: "+ resultSet);
            while (resultSet.next()) {
                UUID currentBeerId = UUID.fromString(resultSet.getString("beer_id"));
                String currentBeerName = resultSet.getString("beer_name");
                Double currentBeerGraduation = resultSet.getDouble("graduation");
                String currentBeerType = resultSet.getString("type");
                String currentBeerDescription = resultSet.getString("description");
                String currentManufacturerName = resultSet.getString("manufacturer_name");
                String currentManufacturerNationality= resultSet.getString("nationality");

                Manufacturer currentManufacturer = new Manufacturer(currentManufacturerName,currentManufacturerNationality);
                Beer currentBeer = new Beer(currentBeerName,currentBeerGraduation,currentBeerType,currentBeerDescription,currentManufacturer);
                currentBeer.setId(currentBeerId);
                foundBeers.add(currentBeer);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerFinder: " + e.getMessage());
            return null;
        }

        return foundBeers;
    }


    @Override
    public boolean addNewBeer(Beer newBeer) {

        String sql = "INSERT INTO beers (beer_id,name,graduation,type,description,manufacturer_id) " +
                "VALUES (?::uuid, ? , ? , ? , ? , ?::uuid ) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1,newBeer.getId().toString());
            preparedStatement.setString(2, newBeer.getName());
            preparedStatement.setDouble(3, newBeer.getGraduation());
            preparedStatement.setString(4, newBeer.getType());
            preparedStatement.setString(5, newBeer.getDescription());
            preparedStatement.setString(6, newBeer.getManufacturer().getId().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Beer:" + resultSet);
            while(resultSet.next()) {
                String returnedName = resultSet.getString("beer_id");
                System.out.println("Recently inserted beer with id: " + returnedName);

                if (newBeer.getId().toString().equals(returnedName)) {
                    System.out.println("EVERYTHING WAS CORRECT. New beer inserted");
                } else {
                    System.out.println("SOMETHING WAS WRONG. Not sure beer inserted");
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE while adding newBeer: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean updateBeer(Beer updatedBeer) {

        String sql = "UPDATE beers " +
                "SET name = ? ," +
                "    graduation = ? ," +
                "    type = ? ," +
                "    description = ? ," +
                "    manufacturer_id = ?::uuid " +
                "WHERE beer_id = ?::uuid " +
                "RETURNING *";

        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, updatedBeer.getName());
            preparedStatement.setDouble(2, updatedBeer.getGraduation());
            preparedStatement.setString(3, updatedBeer.getType());
            preparedStatement.setString(4, updatedBeer.getDescription());
            preparedStatement.setString(5, updatedBeer.getManufacturer().getId().toString());
            preparedStatement.setString(6,updatedBeer.getId().toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                boolean checkName = Objects.equals(resultSet.getString("name"), updatedBeer.getName());
                boolean checkGraduation = resultSet.getDouble("graduation") == updatedBeer.getGraduation();
                boolean checkType= Objects.equals(resultSet.getString("type"), updatedBeer.getType());
                boolean checkDescription = Objects.equals(resultSet.getString("description"), updatedBeer.getDescription());
                boolean checkManufacturerId = Objects.equals(resultSet.getString("manufacturer_id"), updatedBeer.getManufacturer().getId().toString());

                if(checkName && checkGraduation && checkType && checkDescription && checkManufacturerId) return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE during record update:" + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getManufacturerList() {
        ArrayList<String> manufacturerList = new ArrayList<>();

        String sql = "SELECT name " +
                "FROM manufacturers " +
                "ORDER BY name";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the purchase info retrieval: "+ resultSet);
            while (resultSet.next()) {
                String currentManufacturer = resultSet.getString("name");
                manufacturerList.add(currentManufacturer);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return manufacturerList;
    }

    @Override
    public Manufacturer findManufacturer(String manufacturerName) {
        Manufacturer foundManufacturer = null;

        String sql = "SELECT manufacturer_id,name,nationality " +
                "FROM manufacturers " +
                "WHERE name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, manufacturerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the manufacturer finder: "+ resultSet);
            while (resultSet.next()) {
                UUID currentManufacturerId = UUID.fromString(resultSet.getString("manufacturer_id"));
                String currentManufacturerName = resultSet.getString("name");
                String currentManufacturerNationality= resultSet.getString("nationality");

                foundManufacturer = new Manufacturer(currentManufacturerName,currentManufacturerNationality);
                foundManufacturer.setId(currentManufacturerId);

            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return foundManufacturer;
    }

    @Override
    public boolean addNewManufacturer(Manufacturer newManufacturer) {

        String sql = "INSERT INTO manufacturers (manufacturer_id,name,nationality) " +
                "VALUES (?::uuid , ? , ? ) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1,newManufacturer.getId().toString());
            preparedStatement.setString(2, newManufacturer.getName());
            preparedStatement.setString(3, newManufacturer.getNationality());

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Manufacturer:" + resultSet);
            while(resultSet.next()) {
                String returnedName = resultSet.getString("manufacturer_id");
                System.out.println("Recently inserte manufacturer with id: " + returnedName);

                if (newManufacturer.getId().toString().equals(returnedName)) {
                    System.out.println("EVERYTHING WAS CORRECT. New manufacturer inserted");
                } else {
                    System.out.println("SOMETHING WAS WRONG. Not sure manufacturer inserted");
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE while adding newManufacturer: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean updateManufacturer(Manufacturer updatedManufacturer) {

        String sql = "UPDATE manufacturers " +
                "SET name = ? ," +
                "    nationality = ? " +
                "WHERE manufacturer_id = ?::uuid " +
                "RETURNING *";

        String returnedRecord = "";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, updatedManufacturer.getName());
            preparedStatement.setString(2, updatedManufacturer.getNationality());
            preparedStatement.setString(3, updatedManufacturer.getId().toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                boolean checkName = Objects.equals(resultSet.getString("name"), updatedManufacturer.getName());
                boolean checkNationality = Objects.equals(resultSet.getString("nationality"), updatedManufacturer.getNationality());
                if(checkName && checkNationality) return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE during record update:" + e.getMessage());
            return false;
        }
    }
}
