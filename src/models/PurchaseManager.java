/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import ui.PurchaseManagerMenu;

/**
 *
 * @author lunwe
 */
public class PurchaseManager extends User{
    public PurchaseManager(String userId, String name, String password, String role, String contactNumber, String email) {
        super(userId, name, password, "Purchase Manager", contactNumber, email);
    }

    @Override
    public void accessMenu() {
        new PurchaseManagerMenu().setVisible(true);
    }
}
