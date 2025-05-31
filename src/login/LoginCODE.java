package login;

import models.User;
import models.SalesManager;
import models.PurchaseManager;
import models.InventoryManager;
import models.FinanceManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import models.Admin;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lunwe
 */

public class LoginCODE {
    private String UserID;
    private String password;
    private String selectedRole;
    private String selectedDataFile = "userData.txt"; // Can be modified if needed to be dynamic
    
    public LoginCODE() {}

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    // Method to validate user credentials
    public boolean isValidUser() {
        return checkCredentials(UserID, password, selectedRole);
    }

    // Method to check if the provided user ID, password, and role match stored values in the file
    public static boolean checkCredentials(String userID, String password, String role) {
        try (BufferedReader reader = new BufferedReader(new FileReader("userData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String storedID = parts[0];
                    String storedPassword = parts[2];
                    String storedRole = parts[3];
                    if (storedID.equals(userID) && storedPassword.equals(password) && storedRole.equals(role)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Load the user details based on the provided user ID, password, and role
    public User loadUserDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader("userData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    if (parts[0].equals(UserID) && parts[2].equals(password) && parts[3].equals(selectedRole)) {
                        switch (selectedRole) { // Convert to lower case for role matching
                            case "Admin":
                                return new Admin(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                            case "Sales Manager":
                                return new SalesManager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                            case "Purchase Manager":
                                return new PurchaseManager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                            case "Inventory Manager":
                                return new InventoryManager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                            case "Finance Manager":
                                return new FinanceManager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                            default:
                                return null; // Invalid role
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // If no matching user found
    }
}
    

