package com.example.demo1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BankAccountDao {

    public static List<BankAccount> getAccountInfo(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<BankAccount> bankAccounts = new ArrayList<>();
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT bank_accouts.id,currency.id,currency.currency_short,currency.currency_long,bank_accouts.current_cash FROM bank_accouts,currency WHERE currency.id = bank_accouts.currency_id AND user_id = "+userId;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(resultSet.getInt(1));
                bankAccount.setCurrencyId(Integer.parseInt(resultSet.getString(2)));
                bankAccount.setCurrencyShort(resultSet.getString(3));
                bankAccount.setCurrencyLong(resultSet.getString(4));
                bankAccount.setCurrentCash(Float.parseFloat(resultSet.getString(5)));
                bankAccounts.add(bankAccount);
            }
            statement.close();
            return bankAccounts;

            //return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            //logger.log(Level.SEVERE, exception.getMessage());
        }
        statement.close();
        return bankAccounts;
    }


    public static int addBankAccount(int userid, int currency, Float starting_cash, String account_number) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO bank_accouts(`currency_id`,`user_id`,`card_id`,`current_cash`,`account_number`) VALUES(" +currency + ","+ userid +", (SELECT MAX(`id`)+1 FROM `cards`), "+starting_cash+"," + account_number +")";
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
