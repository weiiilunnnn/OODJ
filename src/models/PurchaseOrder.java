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
    private int quantity;
    private String raisedBy;
    private String date;
    private String status;
    private String SupplierID;
    private String SupplierPrice;
    private boolean StockUpdate;
    private boolean Verified;
    
    public PurchaseOrder() {}

    public PurchaseOrder(String poID, String prID, String itemID, int quantity, String raisedBy, String date, String status, String SupplierID, String SupplierPrice, boolean StockUpdate, boolean Verified) {
        this.poID = poID;
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.raisedBy = raisedBy;
        this.date = date;
        this.status = status;
        this.SupplierID = SupplierID;
        this.SupplierPrice = SupplierPrice;
        this.StockUpdate = StockUpdate;
        this.Verified = Verified;
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

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
    }

    public String getSupplierPrice() {
        return SupplierPrice;
    }

    public void setSupplierPrice(String SupplierPrice) {
        this.SupplierPrice = SupplierPrice;
    }

    public boolean isStockUpdate() {
        return StockUpdate;
    }

    public void setStockUpdate(boolean StockUpdate) {
        this.StockUpdate = StockUpdate;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setVerified(boolean Verified) {
        this.Verified = Verified;
    }
    
    
    @Override
    public String toFileFormat() {
        return poID + "," + prID + "," + itemID +"," + quantity + "," + raisedBy + "," + 
                date + ","  + status + "," + SupplierID + "," + SupplierPrice + "," + StockUpdate + "," + Verified;
    }
}
