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
import ui.AdminMenu;

/**
 *
 * @author lunwe
 */
public class Admin extends User{
    private User user;
    
    public Admin(String userId, String name, String password, String role, String contactNumber, String email) {
       super(userId, name, password, "Admin", contactNumber, email);
    }
    
    public Admin(){};
    
    public Admin(String userId, String name){
        super(userId, name);
    };
     
    @Override
    public void accessMenu() {
        new AdminMenu(user).setVisible(true);
    }
    
     @Override
    protected void appendSpecificData(PrintWriter pw) {
        // Admin has no specific extra data, so leave it blank or log role if needed
    }

    // Method to create a New user
    public void registerNewUser(User user) {
        File file = new File("userData.txt");

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("userData.txt created during rsavegistration.");
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
