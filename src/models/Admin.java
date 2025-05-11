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
    public Admin(String userId, String name, String password, String role, String contactNumber, String email) {
       super(userId, name, password, "Admin", contactNumber, email);
    }
     
    @Override
    public void accessMenu() {
        new AdminMenu().setVisible(true);
    }
    
     @Override
    protected void appendSpecificData(PrintWriter pw) {
        // Admin has no specific extra data, so leave it blank or log role if needed
    }

    // Method to create a Sales Manager
    public SalesManager registerSalesManager(String userId, String name, String password, String contactNumber, String email) {
        return new SalesManager(userId, name, password, "Sales Manager", contactNumber, email);
    }
    
    public SalesManager registerPurchaseManager(String userId, String name, String password, String contactNumber, String email) {
        return new SalesManager(userId, name, password, "Purchase Manager", contactNumber, email);
    }
    
    public SalesManager registerInventoryManager(String userId, String name, String password, String contactNumber, String email) {
        return new SalesManager(userId, name, password, "Inventory Manager", contactNumber, email);
    }
    
    public SalesManager registerFinanceManager(String userId, String name, String password, String contactNumber, String email) {
        return new SalesManager(userId, name, password, "Finance Manager", contactNumber, email);
    }
    
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
