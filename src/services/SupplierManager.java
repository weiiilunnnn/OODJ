/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.Supplier;
/**
 *
 * @author lunwe
 */

public class SupplierManager extends MainManager<Supplier>{
    private List<String> tempItemCodes = new ArrayList<>();
    
    public SupplierManager() {
        super("Supplier.txt");
    }
    
    @Override
    protected Supplier parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new Supplier(parts[0], parts[1], parts[2], parts[3]);
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
        String[] columns = {"Supplier ID", "Supplier Name", "Contact Number", "Address"};

        return MainManager.getTableModel(supplierList, columns, supplier -> new Object[]{
            supplier.getSupplierID(), supplier.getSupplierName(), supplier.getContactNo(), supplier.getSupplierAddress(),
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
    
    
    public void addItemCode(String itemCode) {
        if (itemCode != null && !itemCode.isEmpty()) {
            tempItemCodes.add(itemCode);
        }
    }

    public void saveSupplier(String supplierID, String supplierName) {
        try {
            updateSupplierInFile(supplierID, supplierName, tempItemCodes);
            tempItemCodes = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSupplierInFile(String supplierID, String supplierName, List<String> newItemCodes) throws IOException {
        File file = new File("SupplierList.txt");
        List<String> lines = new ArrayList<>();

        boolean supplierFound = false;

        // Read and process existing lines
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(supplierID)) {
                        supplierFound = true;

                        // Merge existing + new item codes (avoid duplicates)
                        Set<String> itemSet = new LinkedHashSet<>();
                        itemSet.addAll(Arrays.asList(parts).subList(2, parts.length)); // existing
                        itemSet.addAll(newItemCodes); // new ones

                        StringBuilder updatedLine = new StringBuilder();
                        updatedLine.append(supplierID).append(",").append(supplierName);
                        for (String item : itemSet) {
                            updatedLine.append(",").append(item);
                        }
                        lines.add(updatedLine.toString());
                    } else {
                        lines.add(line); 
                    }
                }
            }
        }

        if (!supplierFound) {
            StringBuilder newLine = new StringBuilder();
            newLine.append(supplierID).append(",").append(supplierName);
            for (String item : newItemCodes) {
                newLine.append(",").append(item);
            }
            lines.add(newLine.toString());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        }
    }


    public Map<String, String> loadItemMap() throws IOException {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Item.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return map;
    }
    
    public void displaySupplierItems(String supplierID, JTable savedItemTable) throws IOException {
        Map<String, String> itemMap = loadItemMap();

        try (BufferedReader br = new BufferedReader(new FileReader("SupplierList.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(supplierID)) {
                    // Collect item codes from parts[2] onwards (handle variable-length lines)
                    List<String> itemCodes = new ArrayList<>();
                    for (int i = 2; i < parts.length; i++) {
                        itemCodes.add(parts[i].trim());
                    }

                    // Load into table
                    DefaultTableModel model = (DefaultTableModel) savedItemTable.getModel();
                    model.setRowCount(0);
                    for (String code : itemCodes) {
                        String name = itemMap.getOrDefault(code, "Unknown");
                        model.addRow(new Object[]{code, name});
                    }
                    return; // Stop after first match
                }
            }
        }
    }
    
    public void deleteSupplierItem(String supplierID, String itemCode) throws IOException {
        File inputFile = new File("SupplierList.txt");
        File tempFile = new File("SupplierList_temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[0].equals(supplierID)) {
                
                List<String> items = new ArrayList<>(Arrays.asList(parts).subList(2, parts.length));
          
                items.removeIf(code -> code.equalsIgnoreCase(itemCode));
                
                String newLine = parts[0] + "," + parts[1]; 
                for (String item : items) {
                    newLine += "," + item;
                }

                writer.write(newLine);
            } else {               
                writer.write(line);
            }
            writer.newLine();
        }

        reader.close();
        writer.close();

        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file");
        }
    }
    
    public void selectTable(JTable table, JTextField idField, JTextField nameField) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0 && table.getRowCount() > 0 && table.getColumnCount() >= 2) {
                    try {
                        Object idObj = table.getValueAt(row, 0);
                        Object nameObj = table.getValueAt(row, 1);

                        if (idObj != null && nameObj != null) {
                            idField.setText(idObj.toString());
                            nameField.setText(nameObj.toString());
                        }
                    } catch (Exception ex) {
                        System.err.println("Error reading selected row: " + ex.getMessage());
                    }
                }
            }
        });
    }
    
    public void deleteItemFromAllSuppliers(String itemID, String filePath) {
        List<Supplier> updatedSuppliers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String supplierID = parts[0];
                String supplierName = parts[1];
                List<String> itemCodes = new ArrayList<>();

                for (int i = 2; i < parts.length; i++) {
                    if (!parts[i].equals(itemID)) {
                        itemCodes.add(parts[i]);
                    }
                }

                Supplier supplier = new Supplier(supplierID, supplierName, "", "");
                supplier.setItemCodes(itemCodes);
                updatedSuppliers.add(supplier);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Supplier s : updatedSuppliers) {
                writer.write(s.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
