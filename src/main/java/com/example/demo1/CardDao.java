package com.example.demo1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDao {
    public static List<Card>  getCardInfo(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Card> Cards = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id,card_producent_id,number,valid_year,valid_month FROM cards WHERE account_id = "+userId;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card();
                card.setId(resultSet.getInt(1));
                card.setCardProducentId(resultSet.getInt(2));
                card.setNumber(resultSet.getString(3));
                card.setValidYear(resultSet.getInt(4));
                card.setValidMonth(resultSet.getInt(5));
                Cards.add(card);
            }
            statement.close();
            return Cards;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return Cards;
    }



    public static int addBankAccount(int cardProducent, int idAccount, String number, String year, String month) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO cards(`card_producent_id`,`account_id`,`number`,`valid_year`,`valid_month`) VALUES(" +cardProducent + ","+ idAccount +", '"+number+"', '"+year+"','" + month +"')";
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
}
