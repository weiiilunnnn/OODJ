package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.LocalDate;

import models.Item;
import models.Supplier;
import models.PurchaseOrder;
import models.Payment;

public class RecieptReportGenerator {
    private Item item;
    private Supplier supplier;
    private Payment payment;
    private PurchaseOrder po;
    private SupplierManager supplierManager;
    
    private static final String USER_FILE = "userData.txt";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public RecieptReportGenerator(PurchaseOrder po) {
        ItemManager itemManager = new ItemManager();
        supplierManager = new SupplierManager();
        PaymentManager paymentManager = new PaymentManager();
        POManager purchaseorder = new POManager();

        this.item = itemManager.findItemById(po.getItemID());
        System.out.println("item:" + item);
        this.supplier = supplierManager.findSupplierById(po.getSupplierID());
        System.out.println("Fetched Supplier: " + supplier);
        this.payment = paymentManager.findPaymentByPO(po.getPoID());
        this.po = purchaseorder.getById(po.getPoID());
        System.out.println("Supplier: " + supplier.getSupplierID() + "\nItem: " + item.getItemID());
        System.out.println("SupplierItemPrice: " + supplierManager.getItemPrice(supplier.getSupplierID(),item.getItemID()));


    }

    private String findUserNameById(String userID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(userID)) {
                    return parts[1]; // Name
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userID; // fallback to ID
    }

    public String getItemId() { return item.getItemID(); }
    public String getItemName() { return item.getItemName(); }
    public int getQuantity() { return po.getQuantity(); }
    public double getItemPrice() { return supplierManager.getItemPrice(supplier.getSupplierID(),item.getItemID());}
    public String getSupplierId() { return supplier.getSupplierID(); }
    public String getSupplierName() { return supplier.getSupplierName(); }
    public String getSupplierAddress() { return supplier.getSupplierAddress(); }
    public String getSupplierContact() { return supplier.getContactNo(); }
    public String getAccountName() { return payment.getAccountName(); }
    public String getAccountNumber() { return payment.getAccountNumber(); }
    public String getPaymentTime() { return payment.getTimestamp(); }
    public String getPaidBy() { return payment.getUserID(); }
    public double getSubtotal() { return getItemPrice() * getQuantity(); }
    public double getTax() { return getSubtotal() * 0.06; }
    public double getTotal() { return getSubtotal() + getTax(); }
    public String formatCurrency(double value) { return "RM " + df.format(value); }
    
    public models.ReceiptData getReceiptData() {
        models.ReceiptData data = new models.ReceiptData();

        data.setPurchaseOrderID(po.getPoID());
        data.setAccountName(getAccountName());
        data.setAccountNumber(getAccountNumber());
        data.setTimestamp(getPaymentTime());
        data.setPaidByUserID(getPaidBy());

        data.setItemID(getItemId());
        data.setItemName(getItemName());
        data.setQuantity(getQuantity());
        data.setItemPrice(getItemPrice());

        data.setSupplierID(getSupplierId());
        data.setSupplierName(getSupplierName());
        data.setSupplierAddress(getSupplierAddress());
        data.setSupplierContact(getSupplierContact());

        data.setSubtotal(getSubtotal());
        data.setTax(getTax());
        data.setTotal(getTotal());

        return data;
    }
    
    public static File getDefaultFilePath() {
        String fileName = "Reciept" + LocalDate.now() + ".pdf";
        return new File(fileName);
    }

}
