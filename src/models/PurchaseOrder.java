/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author lunwe
 */
public class PurchaseOrder implements ItemDependentRecord{
    private String poID;
    private String prID;
    private String itemID;
    private String itemName;
    private int quantity;
    private String raisedBy;
    private String date;
    private String status;
    
    public PurchaseOrder() {}

    public PurchaseOrder(String poID, String prID, String itemID, String itemName, int quantity, String raisedBy, String date, String status) {
        this.poID = poID;
        this.prID = prID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.raisedBy = raisedBy;
        this.date = date;
        this.status = status;
    }

    public String getPoID() {
        return poID;
    }

    public void setPoID(String poID) {
        this.poID = poID;
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

    @Override
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
        return poID + "," + prID + "," + itemID +"," + itemName + "," + quantity + "," + raisedBy + "," + date + ","  + status;
    }
}
