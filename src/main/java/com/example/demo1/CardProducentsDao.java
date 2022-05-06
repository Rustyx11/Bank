package com.example.demo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardProducentsDao {

    public static List<CardProducents> getCardProducentsList() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<CardProducents> cardProducents = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT id,name FROM `card_producent`";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CardProducents cardProducent = new CardProducents();
                cardProducent.setId(resultSet.getInt(1));
                cardProducent.setName(resultSet.getString(2));
                cardProducents.add(cardProducent);
            }
            statement.close();
            return cardProducents;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return cardProducents;
    }
}
