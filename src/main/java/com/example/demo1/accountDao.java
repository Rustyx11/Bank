package com.example.demo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class accountDao {
    public static List<Account> getAccountInfo(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Account> accounts = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT `id`,`currency_id`,`user_id`,`card_id`,`current_cash`,`account_number` FROM `bank_accouts` WHERE `user_id` = "+userId;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt(1));
                account.setId_currenct(Integer.parseInt(resultSet.getString(2)));
                account.setId_user(Integer.parseInt(resultSet.getString(3)));
                account.setId_card(Integer.parseInt(resultSet.getString(4)));
                account.setCurrent_cash(Float.parseFloat(resultSet.getString(5)));
                account.setAccout_number(resultSet.getString(6));
                accounts.add(account);
            }
            statement.close();
            return accounts;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return accounts;
    }
}
