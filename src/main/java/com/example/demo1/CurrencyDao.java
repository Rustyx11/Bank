package com.example.demo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CurrencyDao {
    public static List<Currency> getCurrencyList() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Currency> currences = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT `id`,`currency_short`,`currency_long` FROM `currency` WHERE 1";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt(1));
                currency.setNameShort(resultSet.getString(2));
                currency.setNameLong(resultSet.getString(3));
                currences.add(currency);
            }
            statement.close();
            return currences;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return currences;
    }
}
