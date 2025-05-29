/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Supplier;
/**
 *
 * @author lunwe
 */

public class SupplierManager extends MainManager<Supplier>{   
    private static final String SUPPLIER_LIST_FILE = "SupplierList.txt";
    FileOperation fo = new FileOperation();
    
    public SupplierManager() {
        super("Supplier.txt");
    }
    
    @Override
    protected Supplier parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            return new Supplier(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
        }
        return null;
    }
    
    @Override
    protected String toLine(Supplier supplier) {
        return supplier.toString();
    }
    
    @Override
    public void delete(String id, List<Supplier> list) {
        list.removeIf(p -> p.getSupplierID().equals(id));
        save(list);
    }
    
    @Override
    public void update(Supplier supplier, List<Supplier> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSupplierID().equals(supplier.getSupplierID())) {
                list.set(i, supplier);
                break;
            }
        }
        save(list);
    }
    
    public DefaultTableModel getSupplierTableModel() {
        List<Supplier> supplierList = load();
        String[] columns = {"Supplier ID", "Supplier Name", "Contact Number", "Address", "Distance (km)"};

        return MainManager.getTableModel(supplierList, columns, supplier -> new Object[]{
            supplier.getSupplierID(), supplier.getSupplierName(), supplier.getContactNo(), supplier.getSupplierAddress(), supplier.getDistance()
        });
    }

    
    public List<String[]> loadItemsFromFile(String filename) {
        List<String[]> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    items.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    public DefaultTableModel getItemTableModel(List<String[]> itemlist) {
        String[] columns = {"Item ID", "Item Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (String[] item : itemlist) {
            if (item.length >= 2) {
                model.addRow(new Object[]{item[0], item[1]});
            }
        }
        return model;
    }
    
    
    // Method to get a list of item IDs supplied by a given supplier ID
    public List<String> getItemIDsBySupplier(String supplierID) {
        List<String> itemIDs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_LIST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(supplierID)) {
                    itemIDs.add(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemIDs;
    }

    // Method to get items (ID and Name) supplied by a given supplier ID
    public Map<String, String> getSupplierItems(String supplierID) {
        List<String> itemIDs = getItemIDsBySupplier(supplierID);
        Map<String, String> itemMap = fo.getItemMap();

        Map<String, String> supplierItems = new HashMap<>();
        for (String itemID : itemIDs) {
            if (itemMap.containsKey(itemID)) {
                supplierItems.put(itemID, itemMap.get(itemID));
            }
        }
        return supplierItems;
    }
    
    public List<String[]> getSupplierItemDetails(String supplierID) {
        List<String[]> itemDetails = new ArrayList<>();

        Map<String, String> itemMap = fo.getItemMap();

        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_LIST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(supplierID)) {
                    String itemID = parts[1];
                    String price = parts[2];
                    String itemName = itemMap.getOrDefault(itemID, "Unknown Item");
                    itemDetails.add(new String[]{itemID, itemName, price});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemDetails;
    }
    
    public void displaySupplierItems(String supplierID, JTable table) throws IOException {
        List<String[]> supplierItems = getSupplierItemDetails(supplierID);
        String[] columns = {"Item ID", "Item Name", "Supplied Price"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (String[] item : supplierItems) {
            model.addRow(item);
        }

        table.setModel(model);
    }
    
    public void addItemCode(String supplierID, String itemID, String price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SupplierList.txt", true))) {
            writer.write(supplierID + "," + itemID + "," + price);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteSupplierItem(String supplierID, String itemID) throws IOException {
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_LIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String currentSupplierID = parts[0].trim();
                    String currentItemID = parts[1].trim();

                    // Keep only lines that are not the one to delete
                    if (!(currentSupplierID.equals(supplierID) && currentItemID.equals(itemID))) {
                        updatedLines.add(line);
                    }
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLIER_LIST_FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        }
    }
    
    public Supplier findSupplierById(String id) {
        List<Supplier> suppliers = load();
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equalsIgnoreCase(id)) {
                return supplier;
            }
        }
        return null;
    }
    
    public double getItemPrice(String supplierID, String itemID) {
        try (BufferedReader br = new BufferedReader(new FileReader("SupplierList.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String sid = parts[0].trim();
                    String iid = parts[1].trim();
                    String priceStr = parts[2].trim();

                    if (sid.equalsIgnoreCase(supplierID) && iid.equalsIgnoreCase(itemID)) {
                        try {
                            return Double.parseDouble(priceStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid price format for " + sid + " and " + iid);
                            return 0.0;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0; // Not found
    }
}
