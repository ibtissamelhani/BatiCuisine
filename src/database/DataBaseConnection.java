package database;

import utils.PropertiesLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {

    private static DataBaseConnection instance;
    private static Connection connection;

    private DataBaseConnection() {
        try {
            Properties properties = PropertiesLoader.readProperties();
            String URL = properties.getProperty("db.url");
            String USER = properties.getProperty("db.user");
            String PASSWORD = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            Class.forName(driver);
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to PostgreSQL database done.");

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Connection failed: " + e.getMessage());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static  DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
