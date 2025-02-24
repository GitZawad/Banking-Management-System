package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    Connection connection;
    Scanner sc;

    public Accounts(Connection connection,Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

     public long openAccount(){
         sc.nextLine();
         System.out.print("Enter your full name : ");
         String name = sc.nextLine();
         System.out.print("Enter your Email : ");
         String email = sc.nextLine();
         System.out.print("Enter the amount you want to insert : ");
         double balance = sc.nextDouble();
         System.out.print("Enter your security pin : ");
         int securityPin = 0;
         while(Integer.toString(securityPin).length() != 4){
             securityPin = sc.nextInt();
             System.out.println();
             if(Integer.toString(securityPin).length() == 4){
                 break;
             } else {
                 System.out.println("Security pin cannot be more than 4 numbers");
                 System.out.print("Enter your security pin : ");
             }
         }

         long accountNumber = generateAccountNumber();

         String query = "INSERT INTO accounts VALUES(?,?,?,?,?);";
         try{
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             preparedStatement.setLong(1,accountNumber);
             preparedStatement.setString(2,name);
             preparedStatement.setString(3,email);
             preparedStatement.setDouble(4,balance);
             preparedStatement.setInt(5, securityPin);
             int affectedRows = preparedStatement.executeUpdate();
             if(affectedRows > 0){
                 System.out.println("Account open successful");
             } else {
                 System.out.println("Unable to register account");
             }
         } catch (SQLException e){
             System.out.println(e.getMessage());
         }
         return accountNumber;
     }

     private long generateAccountNumber() {
         String query = "SELECT* FROM accounts ORDER BY account_number DESC LIMIT 1;";
         try {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
             if (resultSet.next()){
                 long accountNumber = resultSet.getLong("account_number");
                 return accountNumber+1;
             }
             return 1000100;
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
         return 1000100;
     }

    public boolean accountExists(String email){
        String query = "SELECT* FROM accounts WHERE email = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
