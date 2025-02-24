package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Connection connection;
    Scanner sc;

    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void Register(){
        sc.nextLine();
        System.out.print("Enter your full name : ");
        String name = sc.nextLine();
        System.out.print("Enter your Email : ");
        String email  = sc.nextLine();
        System.out.print("Enter your password : ");
        String password = sc.nextLine();

        if(user_exists(email)){
            System.out.println("User already exists.");
            return;
        }

        String query = "INSERT INTO user VALUES(?,?,?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0){
                System.out.println("User Registration Successful.");
            } else {
                System.out.println("User Registration Failed. Please try again later.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String userLogin(String email,String password){
        String query = "SELECT* FROM user WHERE email = ? AND password = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            } else {
                return null;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exists(String email){
        String query = "SELECT* FROM user WHERE email = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
