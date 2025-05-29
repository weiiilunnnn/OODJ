/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author shihongwong
 */

public class Payment {
    private String paymentID;
    private String status;
    private String purchaseOrderID;
    private String accountName;
    private String accountNumber;
    private String userID;
    private String timestamp;
    
    public Payment(){}

    public Payment(String paymentID, String status, String purchaseOrderID,
                   String accountName, String accountNumber, String userID, String timestamp) {
        this.paymentID = paymentID;
        this.status = status;
        this.purchaseOrderID = purchaseOrderID;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.userID = userID;
        this.timestamp = timestamp;
    }

    // For unpaid, only first 3 params are needed
    public Payment(String paymentID, String status, String purchaseOrderID) {
        this(paymentID, status, purchaseOrderID, null, null, null, null);
    }

    // Getters here

    public String getPaymentID() {
        return paymentID;
    }

    public String getStatus() {
        return status;
    }

    public String getPurchaseOrderID() {
        return purchaseOrderID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getUserID() {
        return userID;
    }

    public String getTimestamp() {
        return timestamp;
    }
    
    
    
    //setters here

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPurchaseOrderID(String purchaseOrderID) {
        this.purchaseOrderID = purchaseOrderID;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
