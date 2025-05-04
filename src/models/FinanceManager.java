/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import ui.FinanceManagerMenu;

/**
 *
 * @author lunwe
 */
public class FinanceManager extends User{
    public FinanceManager(String userId, String name, String password, String role, String contactNumber, String email) {
        super(userId, name, password, "Finance Manager", contactNumber, email);
    }

    @Override
    public void accessMenu() {
        new FinanceManagerMenu().setVisible(true);
    }
}
