/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author shihongwong
 */

import models.Payment;
import models.PurchaseOrder;
import models.Item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FinancialReportGenerator {

    private final PaymentManager paymentManager;
    private final POManager poManager;
    private final ItemManager itemManager;
    private final SupplierManager supplierManager; // ✅ New

    public FinancialReportGenerator() {
        this.paymentManager = new PaymentManager();
        this.poManager = new POManager();
        this.itemManager = new ItemManager();
        this.supplierManager = new SupplierManager(); // ✅ New
    }

    public Map<String, ItemSummary> generateReportData(int selectedMonth, int selectedYear) {
        Map<String, ItemSummary> summaryMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Payment> payments = paymentManager.load();
        boolean foundData = false;

        for (Payment payment : payments) {
            if (!"PAID".equalsIgnoreCase(payment.getStatus()) || payment.getTimestamp() == null) continue;

            try {
                LocalDateTime dateTime = LocalDateTime.parse(payment.getTimestamp(), formatter);
                if (dateTime.getMonthValue() == selectedMonth && dateTime.getYear() == selectedYear) {
                    foundData = true;

                    PurchaseOrder po = poManager.getById(payment.getPurchaseOrderID());
                    if (po == null) continue;

                    Item item = itemManager.findItemById(po.getItemID());
                    if (item == null) continue;

                    String itemID = item.getItemID();
                    String itemName = item.getItemName();
                    int quantity = po.getQuantity();
                    String supplierID = po.getSupplierID();

                    double price = supplierManager.getItemPrice(supplierID, itemID); // ✅ Use supplier price
                    double totalCost = quantity * price;

                    ItemSummary summary = summaryMap.getOrDefault(itemID,
                        new ItemSummary(itemID, itemName, 0, 0.0));

                    summary.addQuantity(quantity);
                    summary.addCost(totalCost);

                    summaryMap.put(itemID, summary);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format for payment timestamp: " + payment.getTimestamp());
            }
        }

        return foundData ? summaryMap : null;
    }

    public static class ItemSummary {
        private final String itemID;
        private final String itemName;
        private int totalQuantity;
        private double totalCost;

        public ItemSummary(String itemID, String itemName, int totalQuantity, double totalCost) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.totalQuantity = totalQuantity;
            this.totalCost = totalCost;
        }

        public void addQuantity(int quantity) {
            this.totalQuantity += quantity;
        }

        public void addCost(double cost) {
            this.totalCost += cost;
        }

        public String getItemID() { return itemID; }
        public String getItemName() { return itemName; }
        public int getTotalQuantity() { return totalQuantity; }
        public double getTotalCost() { return totalCost; }
    }
}