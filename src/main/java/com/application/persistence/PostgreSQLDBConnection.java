package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.tools.PropertiesReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PostgreSQLDBConnection implements DatabaseTalker{

    private String database;
    private final String url;
    private final String user = "postgres";
    private final String password;
    private final Connection currentConnection;

    public PostgreSQLDBConnection(String currentDatabase) {

        database = currentDatabase;
        url = "jdbc:postgresql://localhost/" + database;
        password = PropertiesReader.getDBPassword();
        currentConnection = connect();
    }

    /**
     * Connect to the PostgreSQL database
     *
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

        String sql = "SELECT beerid,beers.name AS beer_name,graduation,type,description,manufacturers.name AS manufacturer_name,nationality " +
                "FROM beers " +
                "INNER JOIN manufacturers USING (manufacturer_id) " +
                "WHERE name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(0, beerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from beerFinder: "+ resultSet);
            while (resultSet.next()) {
                UUID currentBeerId = UUID.fromString(resultSet.getString("beerid"));
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
    public boolean checkBeerPresent(Beer newBeer) {

        String sql = "SELECT beers.name AS beerName, manufacturers.name AS manufacturerName " +
                "FROM beers " +
                "INNER JOIN manufacturers USING (manufacturer_id) " +
                "WHERE beers.name = ? AND manufacturers.name = ?";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(0, newBeer.getName());
            preparedStatement.setString(1, newBeer.getManufacturer().getName());

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from checking beer presence: "+ resultSet);
            while (resultSet.next()) {
                String currentBeer = resultSet.getString("beerName");
                String currentManufacturer = resultSet.getString("manufacturerName");
                if(Objects.equals(currentBeer, newBeer.getName()) || currentManufacturer.equals(newBeer.getManufacturer().getName())){
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the checking beer presence: " + e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public boolean addNewBeer(Beer newBeer, UUID manufacturerId, UUID beerId) {

        //Adding the manufacturer first



        return false;
    }

    @Override
    public boolean updateBeer(Beer beerToUpdate) {
        return false;
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

        String sql = "SELECT name,nationality " +
                "FROM manufacturers " +
                "WHERE name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(0, manufacturerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the purchase info retrieval: "+ resultSet);
            while (resultSet.next()) {
                String currentManufacturerName = resultSet.getString("name");
                String currentManufacturerNationality= resultSet.getString("nationality");

                foundManufacturer = new Manufacturer(currentManufacturerName,currentManufacturerNationality);

            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return foundManufacturer;
    }

    @Override
    public boolean addNewManufacturer(Manufacturer newManufacturer) {
        return false;
    }

    @Override
    public boolean updateManufacturer(Manufacturer manufacturerToUpdate) {
        return false;
    }


}
