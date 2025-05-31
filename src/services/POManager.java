/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.PurchaseOrder;

/**
 *
 * @author lunwe
 */
public class POManager extends MainManager<PurchaseOrder>{
    private final String requisitionFile = "PurchaseRequisition.txt";
    private final String poFile = "PurchaseOrder.txt";
    
    
    public POManager() {
        super("PurchaseOrder.txt");
    }
    
    @Override
    protected PurchaseOrder parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 11) {
            return new PurchaseOrder(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6], parts[7], parts[8], Boolean.parseBoolean(parts[9] ), Boolean.parseBoolean(parts[10]));
        }
        return null;
    }
    
    @Override
    protected String toLine(PurchaseOrder po) {
        return po.toFileFormat();
    }

    @Override
    public void delete(String id, List<PurchaseOrder> list) {
        list.removeIf(p -> p.getPoID().equals(id));
        save(list);
    }

    @Override
    public void update(PurchaseOrder po, List<PurchaseOrder> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPoID().equals(po.getPoID())) {
                list.set(i, po);
                break;
            }
        }
        save(list);
    }
    
    public DefaultTableModel getPOTableModel() {
        List<PurchaseOrder> poList = load();
        Map<String, String> itemMap = FileOperation.getItemMap(); // Get item name mapping

        String[] columns = {
            "PO ID", "PR ID", "Item ID", "Item Name", "Restock Qty",
            "PO Raised By", "Purchase Date", "Status", "Supplier ID", "Supplier Price"
        };

        return MainManager.getTableModel(poList, columns, po -> {
            String itemName = itemMap.getOrDefault(po.getItemID(), "Unknown Item");
            return new Object[]{
                po.getPoID(), po.getPrID(), po.getItemID(), itemName,
                po.getQuantity(), po.getRaisedBy(), po.getDate(), po.getStatus(),
                po.getSupplierID(), po.getSupplierPrice(), false, false
            };
        });
    }
    
    /** Load all pending PRs */
    public List<String[]> getPendingRequisitions() {
        List<String[]> pending = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(requisitionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[5].equalsIgnoreCase("Pending")) {
                    pending.add(data);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading requisitions: " + e.getMessage());
        }
        return pending;
    }

    /** Approve a PR and create a PO */
    public boolean approveRequisition(String[] prData, String supplierName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(poFile, true))) {
            String poID = IDGenerator.generateNextID("PO", poFile);
            String prID = prData[0];
            String itemID = prData[1];
            String qty = prData[2];
            String raisedBy = prData[3];
            String date = prData[4];
            String status = "Approved"; // Set PO status

            // Write PO line: POID,PRID,ITMID,Qty,PO RaisedBy,Date,Status
            String poLine = String.join(",", poID, prID, itemID, qty, raisedBy, date, status);
            bw.write(poLine);
            bw.newLine();

            // Update PR status to Approved
            updateRequisitionStatus(prID, "Approved");

            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing PO: " + e.getMessage());
            return false;
        }
    }

    /** Reject a PR and remove it from the requisition file */
    public boolean rejectRequisition(String prID) {
        return updateRequisitionStatus(prID, "Rejected");
    }
    
    /** Update a specific PR's status */
    private boolean updateRequisitionStatus(String requisitionId, String newStatus) {
        File inputFile = new File(requisitionFile);
        File tempFile = new File("temp_" + requisitionFile);
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(requisitionId)) {
                    parts[5] = newStatus;
                    writer.write(String.join(",", parts));
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error updating requisition status: " + e.getMessage());
            return false;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
        return updated;
    }
    
    public static void updateLine(String fileName, String matchValue, int matchColumn, int updateColumn, String newValue) {
        File inputFile = new File(fileName);
        File tempFile = new File("temp_" + fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > updateColumn && parts[matchColumn].equals(matchValue)) {
                    parts[updateColumn] = newValue;
                    writer.write(String.join(",", parts));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating file: " + e.getMessage());
        }
        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
    
    public static void appendLine(String fileName, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage());
        }
    }
    
    /** Remove a requisition from the file by PR ID */
    public static void deleteRequisitionById(String prID) {
        File inputFile = new File("PurchaseRequisition.txt");
        File tempFile = new File("temp_requisition.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(prID + ",")) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error deleting requisition: " + e.getMessage());
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
    
    public boolean generatePurchaseOrder(String prID, String itemID, int quantity, String date,
                                     String supplierID, String price, String raisedBy) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PurchaseOrder.txt", true))) {
            String poID = IDGenerator.generateNextID("PO", "PurchaseOrder.txt");
            String poStatus = "Pending";

            // Create PO object
            PurchaseOrder po = new PurchaseOrder();
            po.setPoID(poID); po.setPrID(prID); po.setItemID(itemID); po.setQuantity(quantity); po.setRaisedBy(raisedBy);
            po.setDate(date); po.setStatus(poStatus); po.setSupplierID(supplierID); po.setSupplierPrice(price);

            // Use toFileFormat() to write the record
            bw.write(po.toFileFormat());
            bw.newLine();

            // Update PR status
            updateRequisitionStatus(prID, "Approved");

            return true;
        } catch (IOException e) {
            System.err.println("Error generating PO: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveAll(List<PurchaseOrder> poList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("PurchaseOrder.txt"))) {
            for (PurchaseOrder po : poList) {
                writer.println(po.toFileFormat());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public PurchaseOrder getById(String poId) {
        if (poId == null) return null;
        List<PurchaseOrder> PO = load();
        System.out.println("Checking PO_list: " + PO);

        for (PurchaseOrder po : PO) {
            System.out.println("Checking PO: " + po.getPoID());
        }

        return PO.stream()
            .filter(po -> poId.trim().equals(po.getPoID().trim()))
            .findFirst()
            .orElse(null);
    }
    
    public boolean updatePOStatus(String poID, String newStatus) {
        List<String> lines = FileOperation.readRawLines("PurchaseOrder.txt");
        boolean updated = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equals(poID)) {
                parts[6] = newStatus;
                lines.set(i, String.join(",", parts));
                updated = true;
                break;
            }
        }

        if (updated) {
            FileOperation.writeRawLines("PurchaseOrder.txt", lines);
        }

        return updated;
    }
    
    public boolean updateStatus(String poID) {
        List<PurchaseOrder> poList = load(); // Load existing POs
        boolean updated = false;

        for (PurchaseOrder po : poList) {
            if (po.getPoID().equals(poID)) {
                po.setStockUpdate(true);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAll(poList); // Save the updated list back to file
            System.out.println("PO status updated successfully.");
        } else {
            System.err.println("PO with ID " + poID + " not found.");
        }

        return updated;
    }
}
