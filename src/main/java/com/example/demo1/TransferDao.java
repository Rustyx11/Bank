package com.example.demo1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

//id_user_from	date	cash	account_number_to	title	recipient	cash_in_new_currency
public class TransferDao {
    public static List<Transfer> getTransferHistory(String AccountFrom) throws SQLException {
        List<Transfer> transfers = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);

            String query = "SELECT transfers.id,`id_user_from`,`date`,`cash`, account_number_from,account_number_to, CASE WHEN account_number_to IN (SELECT bank_accouts.account_number FROM bank_accouts) AND NOT id_user_from in (SELECT bank_accouts.user_id FROM bank_accouts) THEN 'in' WHEN account_number_to IN (SELECT bank_accouts.account_number FROM bank_accouts) AND id_user_from in (SELECT bank_accouts.user_id FROM bank_accouts) THEN 'transfer' ELSE 'out' END AS type, `title`,`recipient`,`cash_in_new_currency`,currency.currency_short FROM `transfers`,bank_accouts,currency WHERE bank_accouts.currency_id = currency.id AND bank_accouts.account_number = transfers.account_number_from AND  ( transfers.account_number_from =  '"+AccountFrom + "' OR transfers.account_number_to = '" + AccountFrom + "')";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(resultSet.getInt(1));
                transfer.setId_user_form(resultSet.getInt(2));
                transfer.setDate(resultSet.getDate(3));
                transfer.setCash(resultSet.getFloat(4));
                transfer.setAccout_number_from(resultSet.getString(5));
                transfer.setAccout_number_to(resultSet.getString(6));
                transfer.setType(resultSet.getString(7));
                transfer.setTitle(resultSet.getString(8));
                transfer.setRecpient(resultSet.getString(9));
                transfer.setCash_incoming(resultSet.getFloat(10));
                transfer.setCurrency_short(resultSet.getString(11));
                transfers.add(transfer);
            }
            statement.close();
            return transfers;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transfers;
    }

    public static int sendTransfer(User user, String AccountFrom, String accountNumber, Float cash, String title, String recipient) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
           // connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            if(checkAccountIsOurBank(accountNumber)){
                String shortcode_accoutTo = currencyShortcode(accountNumber);
                String shortcode_accoutFrom = currencyShortcode(AccountFrom);
                Float rates = 1f;
                if(shortcode_accoutFrom == shortcode_accoutTo){
                     rates = 1f;
                } else{
                     rates =  1f / rates(currencyShortcode(accountNumber));
                }


                String query = "INSERT INTO transfers(id_user_from, date, cash,account_number_to,title,recipient,cash_in_new_currency) VALUES("+user.getId()+", CURRENT_TIMESTAMP(), "+cash+", "+accountNumber+", "+ title+",'"+recipient+"',"+ cash*rates + ")";
                statement.executeUpdate(query);

                query = "UPDATE bank_accouts SET  current_cash = current_cash - " + cash + " WHERE account_number = " + AccountFrom ;
                statement.executeUpdate(query);

                query = "UPDATE bank_accouts SET  current_cash = current_cash + " + cash*rates  + " WHERE account_number = " + accountNumber ;
                statement.executeUpdate(query);
                return 1;
            } else {

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String query = "INSERT INTO transfers(id_user_from, date, cash,account_number_to,title,recipient,cash_in_new_currency) VALUES("+user.getId()+", CURRENT_TIMESTAMP(), "+cash+", "+accountNumber+", "+ title+",'"+recipient+"',1)";
                System.out.print("Q: \n" + query+ "\n\n");
                statement.executeUpdate(query);

                query = "UPDATE bank_accouts SET  current_cash = current_cash - " + cash + " WHERE account_number = " + AccountFrom ;
                statement.executeUpdate(query);
                return 0;
            }
            //String query = "INSERT INTO transfers(id_user_from, date, cash,account_number_to,title,currency_course,cash_in_new_currency) VALUES(?, ?, ?, ?)";
            //statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            //statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String currencyShortcode(String accountNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT currency.currency_short FROM bank_accouts,currency WHERE currency.id = bank_accouts.currency_id AND account_number =  "+accountNumber;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }

            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean checkAccountIsOurBank(String accountNumber) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT 1 FROM `bank_accouts` WHERE `account_number` =  "+accountNumber;
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static float rates(String shortcode) throws Exception {
        String url = "https://api.nbp.pl/api/exchangerates/rates/a/"+ shortcode.toLowerCase() +"/?format=json";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray rates = new JSONArray(myResponse.getString("rates"));
        JSONObject jsonObj = rates.getJSONObject(0);

        return Float.parseFloat(jsonObj.getString("mid"));
    }
}
