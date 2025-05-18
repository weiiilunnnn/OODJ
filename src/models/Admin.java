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

    
}
