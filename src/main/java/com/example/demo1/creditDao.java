package com.example.demo1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class creditDao {
    public static int addCredit(int userid,Float age,String pesel,Float cash, int month, int childer, int workContractMonth, Float AvgPayout, int anotherCreditsInt, int listOfDebtorInt, int workContractInt ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO `Wniosek` (`idUser`, `wiek`, `pesel`, `Kwota_kredytu`, `OKRES`, `il_dzieci`, `pos_kredyt`, `dluznik`, `rodz_um`, `czas_tu`, `sr_zarobki`, `accepted`) VALUES ('"+userid+"', '"+age+"', '"+pesel+"', '"+cash+"', '"+month+"', '"+childer+"', '"+anotherCreditsInt+"', '"+listOfDebtorInt+"', '"+workContractInt+"', '"+workContractMonth+"', '"+AvgPayout+"', '0');";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
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



    public static List<Credit> getAllApplication() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Credit> Credits = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT`id`,idUser,`wiek`,`pesel`,`Kwota_kredytu`,`OKRES`,`il_dzieci`,`pos_kredyt`,`dluznik`,`rodz_um`,`czas_tu`,`sr_zarobki`,`accepted` FROM `Wniosek` WHERE accepted = 0 ";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Credit credit = new Credit();
                credit.setId(resultSet.getInt(1));
                credit.setUserId(resultSet.getInt(2));
                credit.setAge(resultSet.getFloat(3));
                credit.setPesel(resultSet.getString(4));
                credit.setCash(resultSet.getFloat(5));
                credit.setMonth(resultSet.getInt(6));
                credit.setChildren(resultSet.getInt(7));
                credit.setAnotherCreditsInt(resultSet.getInt(8));
                credit.setListOfDebtorInt(resultSet.getInt(9));
                credit.setWorkContractInt(resultSet.getInt(10));
                credit.setWorkContractMonth(resultSet.getInt(11));
                credit.setAvgPayout(resultSet.getFloat(12));
                credit.setaccepted(resultSet.getInt(13));
                Credits.add(credit);
            }
            statement.close();
            return Credits;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return Credits;
    }

    public static List<Credit> getApplication(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Credit> Credits = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT`id`,idUser,`wiek`,`pesel`,`Kwota_kredytu`,`OKRES`,`il_dzieci`,`pos_kredyt`,`dluznik`,`rodz_um`,`czas_tu`,`sr_zarobki`,`accepted` FROM `Wniosek` WHERE idUser =" + userId;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Credit credit = new Credit();
                credit.setId(resultSet.getInt(1));
                credit.setUserId(resultSet.getInt(2));
                credit.setAge(resultSet.getFloat(3));
                credit.setPesel(resultSet.getString(4));
                credit.setCash(resultSet.getFloat(5));
                credit.setMonth(resultSet.getInt(6));
                credit.setChildren(resultSet.getInt(7));
                credit.setAnotherCreditsInt(resultSet.getInt(8));
                credit.setListOfDebtorInt(resultSet.getInt(9));
                credit.setWorkContractInt(resultSet.getInt(10));
                credit.setWorkContractMonth(resultSet.getInt(11));
                credit.setAvgPayout(resultSet.getFloat(12));
                credit.setaccepted(resultSet.getInt(13));
                Credits.add(credit);
            }
            statement.close();
            return Credits;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return Credits;
    }

    public static int acceptCredit(int id,Float cash) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            // connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            String query = "UPDATE Wniosek SET accepted  =1 WHERE id = " + id;
            statement.executeUpdate(query);

             query = "UPDATE bank_accouts SET bank_accouts.current_cash = bank_accouts.current_cash + "+cash+" WHERE account_number = (SELECT bank_accouts.account_number from bank_accouts WHERE bank_accouts.user_id = (SELECT `idUser`FROM Wniosek WHERE id = "+id+") LIMIT 1)";
            statement.executeUpdate(query);

            return 0;

            } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public static int dennyCredit(int id) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            // connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            String query = "UPDATE Wniosek SET accepted  =2 WHERE id = " + id;
            statement.executeUpdate(query);


            return 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}
