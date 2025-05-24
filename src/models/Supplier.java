/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author lunwe
 */
public class Supplier{
    private String supplierID;
    private String supplierName;
    private String ContactNo;
    private String supplierAddress; 
    private double distance;

    public Supplier() {}

    public Supplier(String supplierID, String supplierName, String ContactNo, String supplierAddress, double distance){
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.ContactNo = ContactNo;
        this.supplierAddress = supplierAddress;
        this.distance = distance;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAdress) {
        this.supplierAddress = supplierAdress;
    }
    
    public double getDistance(){
        return distance;
    }
    
    public void setDistance(double distance){
        this.distance = distance;
    }
    
    @Override
    public String toString() {
        return supplierID + "," +  supplierName + "," + ContactNo + "," + supplierAddress + "," + distance;
    } 
    
}
