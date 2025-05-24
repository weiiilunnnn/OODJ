/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author lunwe
 */
public class PurchaseRequisition implements ItemDependentRecord{
    private String prID;
    private String itemID;
    private int quantity;
    private String raisedBy;
    private String date;
    private String status;
    
    public PurchaseRequisition() {}

    public PurchaseRequisition(String prID, String itemID, int quantity, String raisedBy, String dateNeeded, String status) {
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.raisedBy = raisedBy;
        this.date = dateNeeded;
        this.status = status;
    }

    public String getPrID() {
        return prID;
    }

    public void setPrID(String prID) {
        this.prID = prID;
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toFileFormat() {
        return prID + "," + itemID + "," + quantity + "," + raisedBy + "," + date + "," + status;
    }

}
