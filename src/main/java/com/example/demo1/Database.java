package com.example.demo1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://h25.seohost.pl:3306/srv42082_java_1";
    private static final String DB_USER = "srv42082_java_1";
    private static final String DB_PASSWORD = "qwerty123$";

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
            connection = DriverManager.getConnection(DB_CONNECTION+"?user="+DB_USER+ "&password=" + DB_PASSWORD);
            return connection;
        } catch  (SQLException exception)  {
            System.out.println("3: ");
        }

        return connection;
    }

}