/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author lunwe
 */
public class Item {
    private String itemID;
    private String itemName;
    private int itemQty;
    private double itemPrice;
    private int reorderLevel;
    
    // Constructor initializes item attributes
    public Item(String itemID, String itemName, int itemQty, double itemPrice, int reorderLevel) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
        this.reorderLevel = reorderLevel;
    }
    
    // Getter and Setter methods (Encapsulation)
    public String getItemID() { 
        return itemID; 
    }
    
    public void setItemID(String itemID) { 
        this.itemID = itemID; 
    }

    public String getItemName() { 
        return itemName; 
    }
    
    public void setItemName(String itemName) { 
        this.itemName = itemName; 
    }

    public int getItemQty() { 
        return itemQty; 
    }
    
    public void setItemQty(int itemQty) { 
        this.itemQty = itemQty; 
    }

    public double getItemPrice() { 
        return itemPrice; 
    }
    
    public void setItemPrice(double itemPrice) { 
        this.itemPrice = itemPrice; 
    }

    public int getReorderLevel() { 
        return reorderLevel; 
    }
    
    public void setReorderLevel(int reorderLevel) { 
        this.reorderLevel = reorderLevel; 
    }
    
    /**
     * Derived attribute: automatically calculates flag status.
     * Returns "OK" if quantity > ROL, else "Restock"
     */
    public String getFlagStatus() {
        return (itemQty > reorderLevel) ? "OK" : "Restock";
    }
    
    /**
     * Formats item data for saving into text file (persistent storage).
     */
    public String toDataString() {
        return itemID + "," + itemName + "," + itemQty + "," + itemPrice + "," + reorderLevel + "," + getFlagStatus();
    }

}
