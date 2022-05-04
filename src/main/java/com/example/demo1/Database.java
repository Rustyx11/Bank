package com.example.demo1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database
 * @author Julian Jupiter
 *
 */
public class Database {

    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://192.168.100.109:3306/javafxsample";
    private static final String DB_USER = "springuser";
    private static final String DB_PASSWORD = "qwerty";

    private Database() {

    }

    public static Connection getDBConnection() throws SQLException {
        Connection connection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException exception) {
            System.out.println("1: ");
            logger.log(Level.SEVERE, exception.getMessage());
        }

        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException exception) {
            System.out.println("2: ");
            logger.log(Level.SEVERE, exception.getMessage());
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.100.109:3306/javafxsample?user=springuser&password=qwerty");
            return connection;
        } catch  (SQLException exception)  {
            System.out.println("3: ");
        }

        return connection;
    }

}