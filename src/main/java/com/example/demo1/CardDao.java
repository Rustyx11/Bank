package com.example.demo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
