/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author lunwe
 */
public abstract class User {
    protected String UserID;
    protected String Name;
    protected String Password;
    protected String role;
    protected String contactNumber;
    protected String email;
    private static final String USER_DATA_FILE = "userData.txt";
    
    
    public abstract void accessMenu();

    public User(String UserID, String Name, String Password, String role, String contactNumber, String email) {
        this.UserID = UserID;
        this.Name = Name;
        this.Password = Password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.email = email;
    }
    
    public User(String Name, String UserID) {
        this.Name = Name;
        this.UserID = UserID;
    }
    
    public User(){}

    public void saveData(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        pw.print(this.UserID + "," +
                this.Name + "," +
                this.Password + "," +
                this.role + "," +
                this.contactNumber + "," +
                this.email);
        appendSpecificData(pw);
        pw.println(); 
        pw.flush();
        pw.close();
    }

    protected void appendSpecificData(PrintWriter pw) {} //Override
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void registerNewUser(User user) {
        File file = new File(USER_DATA_FILE);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("userData.txt created during registration.");
            } catch (IOException e) {
                System.err.println("Failed to create userData.txt: " + e.getMessage());
                return;
            }
        }

        // Append new user to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String dataLine = String.join(",",
                user.getUserID(),
                user.getName(),
                user.getPassword(),
                user.getRole(),
                user.getEmail(),
                user.getContactNumber()
            );
            writer.write(dataLine);
            writer.newLine();
            System.out.println("User registered successfully.");
        } catch (IOException e) {
            System.err.println("Failed to write to userData.txt: " + e.getMessage());
        }
    }
}
