package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountsManager {
    Connection connection;
    Scanner sc;

    public AccountsManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void debitMoney(String email){
        System.out.print("Enter account number : ");
        int accountNumber = sc.nextInt();
        System.out.print("Enter security pin : ");
        int securityPin = sc.nextInt();
        System.out.print("Enter the balance you want to debit : ");
        double balance = sc.nextDouble();


        String query = "SELECT balance FROM accounts WHERE email = ? AND security_pin = ?;";
        double currentBalance = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currentBalance = resultSet.getDouble("balance");
            }

            if(balance > currentBalance){
                System.out.println("Invalid balance.");
            } else {
                query = "UPDATE accounts SET balance = ? WHERE account_number = ? AND security_pin = ?;";
                PreparedStatement preparedStatementDebit = connection.prepareStatement(query);
                preparedStatementDebit.setDouble(1,currentBalance -balance);
                preparedStatementDebit.setInt(2,accountNumber);
                preparedStatementDebit.setInt(3,securityPin);
                int affectedRows = preparedStatementDebit.executeUpdate();
                if(affectedRows > 0){
                    System.out.println("Balance debited successfully.");
                } else {
                    System.out.println("Balance debit failed.");
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void creditMoney(String email){
        System.out.print("Enter account number : ");
        int accountNumber = sc.nextInt();
        System.out.print("Enter security pin : ");
        int securityPin = sc.nextInt();
        System.out.print("Enter the balance you want to credit : ");
        double balance = sc.nextDouble();

        String query = "SELECT balance FROM accounts WHERE email = ? AND security_pin = ?;";
        double currentBalance = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setInt(2,securityPin);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currentBalance = resultSet.getDouble("balance");
            }
            query = "UPDATE accounts SET balance = ? WHERE account_number = ? AND security_pin = ?;";

            PreparedStatement preparedStatementCredit = connection.prepareStatement(query);
            preparedStatementCredit.setDouble(1,currentBalance+balance);
            preparedStatementCredit.setInt(2,accountNumber);
            preparedStatementCredit.setInt(3,securityPin);
            int affectedRows = preparedStatementCredit.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Balance credited successfully");
            } else {
                System.out.println("Balance failed to be credited.");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void transferMoney(String email){
        System.out.print("Enter account number : ");
        int accountNumber = sc.nextInt();
        System.out.print("Enter security pin : ");
        int securityPin = sc.nextInt();
        System.out.print("Enter the balance you want to transfer : ");
        double balance = sc.nextDouble();
        System.out.print("Enter the account number you want to transfer balance to : ");
        int transferAccountNumber = sc.nextInt();

        String query = "SELECT balance FROM accounts WHERE email = ? AND security_pin = ?;";
        double currentBalance = 0;

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                currentBalance = resultSet.getDouble("balance");
            }

            if(balance > currentBalance){
                System.out.println("Invalid balance.");
            } else {
                String creditQuery = "UPDATE accounts SET balance = ? WHERE account_number = ? AND security_pin = ?;";

                PreparedStatement preparedStatementCredit = connection.prepareStatement(creditQuery);
                preparedStatementCredit.setDouble(1,currentBalance-balance);
                preparedStatementCredit.setInt(2,accountNumber);
                preparedStatementCredit.setInt(3,securityPin);
                int affectedCreditRows = preparedStatementCredit.executeUpdate();

                String debitQuery = "UPDATE accounts SET balance = ? WHERE account_number = ? AND security_pin = ?;";

                PreparedStatement preparedStatementDebit = connection.prepareStatement(debitQuery);
                preparedStatementDebit.setDouble(1,balance+currentBalance);
                preparedStatementDebit.setInt(2,transferAccountNumber);
                preparedStatementDebit.setInt(3,securityPin);
                int affectedDebitRows = preparedStatementDebit.executeUpdate();

                if(affectedDebitRows > 0 && affectedCreditRows > 0){
                    System.out.println("Transaction Successful");
                    connection.commit();
                } else {
                    System.out.println("Transaction Failed.");
                    connection.rollback();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void checkBalance(String email){
        System.out.print("Enter account number : ");
        int accountNumber = sc.nextInt();

        System.out.print("Enter security pin : ");
        int securityPin = sc.nextInt();

        String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,accountNumber);
            preparedStatement.setInt(2,securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance : "+balance);
            } else {
                System.out.println("No balance available.");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
