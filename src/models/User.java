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
    private String UserID;
    private String Name;
    private String Password;
    private String role;
    private String contactNumber;
    private String email;
    
    
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
    
    
}
