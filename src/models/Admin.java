/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

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
}
