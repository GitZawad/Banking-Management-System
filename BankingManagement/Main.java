package BankingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String URl = "jdbc:mysql://localhost:3306/banking_system";
        String username = "root";
        String password = "1234";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String email;
        long accountNumber;
        try (Connection connection = DriverManager.getConnection(URl,username,password)){
            System.out.println("Database Connected Successfully");

            Scanner sc = new Scanner(System.in);
            while(true){
                System.out.println("Welcome to Banking Management System.");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option : ");
                int choice = sc.nextInt();
                User user = new User(connection,sc);
                Accounts accounts = new Accounts(connection,sc);
                AccountsManager accountsManager = new AccountsManager(connection,sc);

                switch(choice) {
                    case 1 :
                        user.Register();
                        break;
                    case 2 :
                        System.out.print("Email : ");
                        email = sc.next();
                        System.out.print("Password : ");
                        String userPassword = sc.next();
                        if(!user.user_exists(email)){
                            System.out.println("User doesn't exist. Please register first.");
                            break;
                        } else {
                            email = user.userLogin(email,userPassword);
                            if(email != null){
                                if(!accounts.accountExists(email)){
                                    System.out.println("User logged in successfully!");
                                    sc.nextLine();
                                    System.out.println("1. Open a bank account.");
                                    System.out.println("2. Exit");
                                    int choice2 = sc.nextInt();
                                    if(choice2 == 1){
                                        accountNumber = accounts.openAccount();
                                        System.out.println("Your account number is "+accountNumber);
                                        break;
                                    } else if(choice2 == 2){
                                        break;
                                    } else {
                                        System.out.println("Invalid choice.");
                                        break;
                                    }
                                } else {
                                    System.out.println("1. Debit Money");
                                    System.out.println("2. Credit Money");
                                    System.out.println("3. Transfer Money");
                                    System.out.println("4. Check Balance");
                                    System.out.println("5. Log Out");
                                    System.out.print("Choose an option : ");
                                    int choice3 = sc.nextInt();

                                    switch(choice3){
                                        case 1 :
                                            accountsManager.debitMoney(email);
                                            break;
                                        case 2 :
                                            accountsManager.creditMoney(email);
                                            break;
                                        case 3 :
                                            accountsManager.transferMoney(email);
                                            break;
                                        case 4 :
                                            accountsManager.checkBalance(email);
                                            break;
                                        case 5 :
                                            System.out.println("Logged out successfully.");
                                            break;
                                        default:
                                            System.out.println("Invalid choice.");
                                            break;
                                    }
                                    break;
                                }
                            } else {
                                System.out.println("Email or password is incorrect.");
                                break;
                            }
                        }
                    case 3 :
                        System.out.println("Exiting system.");
                        System.out.println("Thanks for using Banking Management System. 😊");
                       return;
                    default :
                        System.out.println("Invalid Choice.");
                        break;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (InputMismatchException e){
            System.out.println("Invalid choice");
        }
    }
}