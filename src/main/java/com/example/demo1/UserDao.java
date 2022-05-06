package com.example.demo1;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public boolean userLogin(String username,String password) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id, username, last_name, first_name, password,admin_permission FROM user WHERE username = '"+username+"' AND password = '"+password+"'";
            System.out.print(query);
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setFirstName(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setAdmin_permission(resultSet.getBoolean(6));
                users.add(user);
            }

            System.out.print("U1+ "+ users.size());
            if(users.size()>=1){
                return true;
            } else {
                return false;
            }
            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return users.isEmpty() ? false : true;
    }

    public static List<User> getClientsList() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id, username, last_name, first_name, password, admin_permission FROM user WHERE admin_permission = 0";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setFirstName(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setAdmin_permission(resultSet.getBoolean(6));
                users.add(user);
            }
            return users;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return users;
    }


    public User getInfoUser(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id, username, last_name, first_name, password, admin_permission FROM user WHERE username = '"+username+"'";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setFirstName(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setAdmin_permission(resultSet.getBoolean(6));
                users.add(user);
            }
            return users.get(0);

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return users.get(0);
    }

    public static boolean userExists(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id, username, last_name, first_name, password FROM user WHERE username = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setFirstName(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                users.add(user);
            }

            return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return users.isEmpty() ? false : true;
    }

    public static int saveUser(String username, String last_name, String first_name, String password, String admin_permission) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Boolean admin_permission_bool = false;
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            if(admin_permission == "Admin"){
                admin_permission_bool = true;
            }
            String query = "INSERT INTO user(username, last_name, first_name, password,admin_permission) VALUES('" +username + "','"+ last_name +"', '"+first_name +"', '"+password+"'," + admin_permission_bool +")";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            if (null != connection) {
                connection.rollback();
            }
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }

            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return 0;
    }

}