/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.List;

/**
 *
 * @author lunwe
 */
public class Supplier{
    private String supplierID;
    private String supplierName;
    private List<String> itemCodes;
    private String ContactNo;
    private String supplierAddress; 

    public Supplier() {}

    public Supplier(String supplierID, String supplierName, String ContactNo, String supplierAddress){
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.ContactNo = ContactNo;
        this.supplierAddress = supplierAddress;
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

    @Override
    public String toString() {
        return supplierID + "," +  supplierName + "," + ContactNo + "," + supplierAddress;
    }

    public String toDataString() {
        // Join item codes with semicolon (or any delimiter you prefer)
        String items = (itemCodes == null || itemCodes.isEmpty()) ? "" : String.join(";", itemCodes);

        return supplierID + "," + supplierName + "," + items;
    }  
    
    // Implementing the interface methods
    public String getItemID() {
        // Assuming you want to remove based on the first item (if multiple exist)
        return null;
    }

    public String getItemName() {
        return supplierName; // Or you can return something else more item-related
    }

    public String toFileFormat() {
        String items = itemCodes == null ? "" : String.join(",", itemCodes);
        return supplierID + "," + supplierName + (items.isEmpty() ? "" : "," + items);
    }

    public boolean removeItemIfExists(String itemID) {
        return itemCodes != null && itemCodes.remove(itemID);
    }
    
    public List<String> getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(List<String> itemCodes) {
        this.itemCodes = itemCodes;
    }

    public void removeItemCode(String itemId) {
        if (itemCodes != null) {
            itemCodes.remove(itemId);
        }
    }
    
}
