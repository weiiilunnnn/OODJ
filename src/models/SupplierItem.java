/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author lunwe
 */
public class SupplierItem {
    private String supplierID;
    private String itemID;
    private double supplyPrice;

    public SupplierItem(String supplierID, String itemID, double supplyPrice) {
        this.supplierID = supplierID;
        this.itemID = itemID;
        this.supplyPrice = supplyPrice;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    @Override
    public String toString() {
        return supplierID + "," + itemID + "," + supplyPrice;
    }
}
