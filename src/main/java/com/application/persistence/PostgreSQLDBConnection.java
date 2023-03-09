package com.application.persistence;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.tools.PropertiesReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
        return null;
    }

    @Override
    public Beer findBeer() {
        return null;
    }

    @Override
    public boolean addNewBeer(Beer newBeer) {
        return false;
    }

    @Override
    public boolean updateBeer(Beer beerToUpdate) {
        return false;
    }

    @Override
    public ArrayList<String> getManufacturerList() {
        return null;
    }

    @Override
    public Manufacturer findManufacturer(String manufacturerName) {
        return null;
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
