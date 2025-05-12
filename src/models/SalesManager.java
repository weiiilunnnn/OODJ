/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.awt.List;
import ui.SalesManagerMenu;

/**
 *
 * @author lunwe
 */
public class SalesManager extends User{
    private User user;
    
    public SalesManager(String userId, String name, String password, String role, String contactNumber, String email) {
        super(userId, name, password, "Sales Manager", contactNumber, email);
    }

    @Override
    public void accessMenu() {
        new SalesManagerMenu(user).setVisible(true);
    }
}
