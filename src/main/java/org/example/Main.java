package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    // 1. Fixed URL to map your exact database (jdbbcapp) and added required secure keys bypass

    private static final String URL = "jdbc:mysql://localhost:3306/jdbbcapp?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";

    // 2. Kept the password blank to match your local server authorization profile
    private static final String PASSWORD = "";

    // 3. Changed 'Main' to lowercase 'main' so Java detects the entry point properly
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        // 4. Passed parameters inside the connection request block
        try (Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD)){
            System.out.println("Successfully Connected to MySQL Database via JDBC! ");

            while(true){
                System.out.println("\n--- Student Database manager ----");
                System.out.println("1 : View All Students");
                System.out.println("2 : Add new Students ");
                System.out.println("3 : Exit ");
                System.out.print("Choose an Option : ");
                int choice  = sc.nextInt();

                if(choice == 1){
                    viewStudents(connection);
                } else if (choice == 2){
                    addStudent(connection , sc);
                } else if (choice == 3){
                    System.out.println("Exiting Application...");
                    break;
                } else if (choice == 4) {
                    System.out.println("Existing Application....");
                    break;
                } else {
                    System.out.println("Invalid Input  ,  Please type a Number Between 1 and 4. ");
                }
            }
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    private static void viewStudents(Connection connection) throws Exception{
        String sql = "SELECT * FROM studentinfo";
        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            System.out.println("\nID \t Name \t Age \t City");
            while(resultSet.next()){
                // 5. Fixed column call from "city" to your database name "scity"
                System.out.println(resultSet.getInt("id") + " \t " +
                        resultSet.getString("sname") + " \t " +
                        resultSet.getInt("SAGE") + " \t " +
                        resultSet.getString("scity"));
            }
        }
    }

    private static void addStudent(Connection connection, Scanner sc) throws Exception{
        System.out.print("Enter ID : ");
        int id = sc.nextInt();
        sc.nextLine(); // clears buffer
        System.out.print("Enter Name : ");
        String name = sc.nextLine();
        System.out.print("Enter Age : ");
        int age = sc.nextInt();
        sc.nextLine(); // clear buffer
        System.out.print("Enter City : ");
        String city = sc.nextLine();

        // 6. Updated SQL structure string to explicitly process the 4th parameter (scity)
        String sql = "INSERT INTO studentinfo (id, sname , SAGE, scity) values (? , ? , ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.setString(4, city);
            statement.executeUpdate();
            System.out.println("Student Record Added Successfully by aditi zii");
        }catch (java.sql.SQLIntegrityConstraintViolationException e ){
            System.out.println("Error !! A student with ID  "  + id + " already exists");


        }
    }
}