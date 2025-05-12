/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import ui.InventoryManagerMenu;

/**
 *
 * @author lunwe
 */
public class InventoryManager extends User{
    private User user;
    
    public InventoryManager(String userId, String name, String password, String role, String contactNumber, String email) {
        super(userId, name, password, "Inventory Manager", contactNumber, email);
    }

    @Override
    public void accessMenu() {
        new InventoryManagerMenu(user).setVisible(true);
    }
}
