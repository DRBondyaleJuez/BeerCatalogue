package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.utils.PropertiesReader;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Provides object of the class in charged of the interaction with the database. In this case a PostgreSQL database. This
 * class implements DatabaseTalker the interface providing the template for the abstract methods that require implementation
 * to fulfill this application's requirements.
 */
public class PostgreSQLDBConnection implements DatabaseTalker{

    private String database;
    private final String url;
    private final String user;
    private final String password;
    private final Connection currentConnection;

    /**
     * This is the constructor which requires the name of the database to be instantiated
     * @param currentDatabase String name of the database
     */
    public PostgreSQLDBConnection(String currentDatabase) {

        user = PropertiesReader.getDBUser();
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

        String sql = "SELECT beer_name " +
                "FROM beers " +
                "ORDER BY beer_name";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the purchase info retrieval: "+ resultSet);
            while (resultSet.next()) {
                String currentBeer = resultSet.getString("beer_name");
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

        String sql = "SELECT beer_name,graduation,type,description,manufacturer_name,nationality " +
                "FROM beers " +
                "INNER JOIN manufacturers USING (manufacturer_name) " +
                "WHERE beer_name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, beerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from beerFinder: "+ resultSet);
            while (resultSet.next()) {
                String currentBeerName = resultSet.getString("beer_name");
                Double currentBeerGraduation = resultSet.getDouble("graduation");
                String currentBeerType = resultSet.getString("type");
                String currentBeerDescription = resultSet.getString("description");
                String currentManufacturerName = resultSet.getString("manufacturer_name");
                String currentManufacturerNationality= resultSet.getString("nationality");

                Manufacturer currentManufacturer = new Manufacturer(currentManufacturerName,currentManufacturerNationality);
                Beer currentBeer = new Beer(currentBeerName,currentBeerGraduation,currentBeerType,currentBeerDescription,currentManufacturer);
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

        String sql = "INSERT INTO beers (beer_name,graduation,type,description,manufacturer_name) " +
                "VALUES ( ? , ? , ? , ? , ? ) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, newBeer.getName());
            preparedStatement.setDouble(2, newBeer.getGraduation());
            preparedStatement.setString(3, newBeer.getType());
            preparedStatement.setString(4, newBeer.getDescription());
            preparedStatement.setString(5, newBeer.getManufacturer().getName());

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Beer:" + resultSet);
            while(resultSet.next()) {
                String returnedName = resultSet.getString("beer_name");
                System.out.println("Recently inserted beer with name: " + returnedName);

                if (newBeer.getName().equals(returnedName)) {
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
    public boolean updateBeer(Beer oldBeer, Beer updatedBeer) {

        String sql = "UPDATE beers " +
                "SET beer_name = ? ," +
                "    graduation = ? ," +
                "    type = ? ," +
                "    description = ? ," +
                "    manufacturer_name = ?" +
                "WHERE beer_name = ? AND manufacturer_name = ? " +
                "RETURNING *";

        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, updatedBeer.getName());
            preparedStatement.setDouble(2, updatedBeer.getGraduation());
            preparedStatement.setString(3, updatedBeer.getType());
            preparedStatement.setString(4, updatedBeer.getDescription());
            preparedStatement.setString(5, updatedBeer.getManufacturer().getName());
            preparedStatement.setString(6, oldBeer.getName());
            preparedStatement.setString(7,oldBeer.getManufacturer().getName());

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                boolean checkName = Objects.equals(resultSet.getString("beer_name"), updatedBeer.getName());
                boolean checkGraduation = resultSet.getDouble("graduation") == updatedBeer.getGraduation();
                boolean checkType= Objects.equals(resultSet.getString("type"), updatedBeer.getType());
                boolean checkDescription = Objects.equals(resultSet.getString("description"), updatedBeer.getDescription());
                boolean checkManufacturerId = Objects.equals(resultSet.getString("manufacturer_name"), updatedBeer.getManufacturer().getName());

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

        String sql = "SELECT manufacturer_name " +
                "FROM manufacturers " +
                "ORDER BY manufacturer_name";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the purchase info retrieval: "+ resultSet);
            while (resultSet.next()) {
                String currentManufacturer = resultSet.getString("manufacturer_name");
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

        String sql = "SELECT manufacturer_name,nationality " +
                "FROM manufacturers " +
                "WHERE manufacturer_name = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, manufacturerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the manufacturer finder: "+ resultSet);
            while (resultSet.next()) {
                String currentManufacturerName = resultSet.getString("manufacturer_name");
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

        String sql = "INSERT INTO manufacturers (manufacturer_name,nationality) " +
                "VALUES ( ? , ? ) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, newManufacturer.getName());
            preparedStatement.setString(2, newManufacturer.getNationality());

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Manufacturer:" + resultSet);
            while(resultSet.next()) {
                String returnedName = resultSet.getString("manufacturer_name");
                System.out.println("Recently inserted manufacturer with name: " + returnedName);

                if (newManufacturer.getName().equals(returnedName)) {
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
    public boolean updateManufacturer(String oldName, Manufacturer updatedManufacturer) {

        String sql = "UPDATE manufacturers " +
                "SET manufacturer_name = ? ," +
                "    nationality = ? " +
                "WHERE manufacturer_name = ?" +
                "RETURNING *";

        String returnedRecord = "";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, updatedManufacturer.getName());
            preparedStatement.setString(2, updatedManufacturer.getNationality());
            preparedStatement.setString(3, oldName);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                boolean checkName = Objects.equals(resultSet.getString("manufacturer_name"), updatedManufacturer.getName());
                boolean checkNationality = Objects.equals(resultSet.getString("nationality"), updatedManufacturer.getNationality());
                if(checkName && checkNationality) return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE during record update:" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createNewUser(String username, byte[] password, boolean adminStatus, String manufacturerName) {
        String sql = "INSERT INTO users (username,password,status) " +
                "VALUES ( ? , ? , ?) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setBinaryStream(2,new ByteArrayInputStream(password));
            preparedStatement.setBoolean(3, adminStatus);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Manufacturer:" + resultSet);

            while(resultSet.next()) {
                String returnedName = resultSet.getString("username");
                System.out.println("Recently inserted manufacturer with name: " + returnedName);

                if (username.equals(returnedName)) {
                    System.out.println("EVERYTHING WAS CORRECT. New user inserted");
                } else {
                    System.out.println("Unable to insert new user correctly");
                    return false;
                }
            }

            return connectUserAndManufacturer(username, manufacturerName);

        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE while adding newManufacturer: " + e.getMessage());
            return false;
        }
    }

    private boolean connectUserAndManufacturer(String username,String manufacturerName){

        String sql = "INSERT INTO authorizations (username,manufacturer_name) " +
                "VALUES ( ? , ? ) " +
                "RETURNING *";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, manufacturerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This is the result set from adding new Manufacturer:" + resultSet);
            while(resultSet.next()) {
                String returnedManufacturerName = resultSet.getString("manufacturer_name");
                String returnedUsername = resultSet.getString("username");
                System.out.println("Recently inserted manufacturer with name: " + returnedManufacturerName);

                if (manufacturerName.equals(returnedManufacturerName) && username.equals(returnedUsername)) {
                    System.out.println("EVERYTHING WAS CORRECT. User and manufacturer connected in authorization table");
                } else {
                    System.out.println("SOMETHING WAS WRONG. Not sure user and manufacturer connected");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE while adding newManufacturer: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public byte[] getPassword(String username) {
        byte[] returnedPassword = null;

        String sql = "SELECT password " +
                "FROM users " +
                "WHERE username = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the manufacturer finder: "+ resultSet);
            while (resultSet.next()) {
                returnedPassword = resultSet.getBytes("password");
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return returnedPassword;
    }

    @Override
    public String checkManufacturerNameForAuthorization(String username) {
        String returnedName = null;

        String sql = "SELECT manufacturer_name " +
                "FROM authorizations " +
                "WHERE username = ? ";
        try {
            PreparedStatement preparedStatement = currentConnection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("This corresponds to the result set from the manufacturer finder: "+ resultSet);
            while (resultSet.next()) {
                returnedName = resultSet.getString("manufacturer_name");
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR MESSAGE of the beerList retrieval: " + e.getMessage());
            return null;
        }

        return returnedName;
    }
}
