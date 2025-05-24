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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import models.PurchaseOrder;
import models.PurchaseRequisition;

/**
 *
 * @author lunwe
 */
public class FileOperation {
    private static final String ITEM_FILE = "Item.txt";
    public static final String PO_FILE = "PurchaseOrder.txt";
    public static final String PR_FILE = "PurchaseRequisition.txt";
    public static final String SUPPLIER_FILE = "Supplier.txt";
    public static final String SPLIST_FILE = "SupplierList.txt";
    public static final String USER_FILE = "userData.txt";
    
    public static void ensureFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create an empty file
                System.out.println("File created: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
                e.printStackTrace();
            }
        }
    }
    
    public static void initAllFiles() {
        ensureFileExists(ITEM_FILE);
        ensureFileExists(PO_FILE);
        ensureFileExists(PR_FILE);
        ensureFileExists(SUPPLIER_FILE);
        ensureFileExists(SPLIST_FILE);
        ensureFileExists(USER_FILE);
    }
    
    public static void WriteFile(Object obj) {
        Class<?> classObj = obj.getClass();
        String fileName = classObj.getSimpleName() + ".txt";
        File file = new File(fileName);

        try {
            boolean fileExists = file.exists();
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

            Field[] fields = classObj.getDeclaredFields();

            // Write CSV header only if file is new
            if (!fileExists) {
                StringBuilder header = new StringBuilder();
                for (Field field : fields) {
                    header.append(field.getName()).append(",");
                }
                pw.println(header.substring(0, header.length() - 1)); // remove trailing comma
            }

            // Write object data row
            StringBuilder row = new StringBuilder();
            for (Field field : fields) {
                String getterName = "get" + capitalize(field.getName());
                try {
                    Method getter = classObj.getMethod(getterName);
                    Object value = getter.invoke(obj);
                    row.append(value).append(",");
                } catch (NoSuchMethodException ignored) {}
            }
            pw.println(row.substring(0, row.length() - 1)); // remove trailing comma
            pw.close();

        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //Method to read CSV file and return table model
    public static DefaultTableModel ReadFile(Class<?> classObj) {
        String fileName = classObj.getSimpleName() + ".txt";
        DefaultTableModel model = new DefaultTableModel();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] headers;

            // Manually define headers if the file has no header
            if (classObj == PurchaseRequisition.class) {
                headers = new String[]{"PRID", "ItemID", "ItemName", "Quantity", "RaisedBy", "DateNeeded", "Status"};
            } //else if (classObj == SomeOtherClass.class) {
                //headers = new String[]{"Column1", "Column2"}; // adjust as needed
            //} 
            else {
                // Default fallback: can't process file without a header
                System.err.println("No header defined for: " + classObj.getSimpleName());
                return model;
            }

            model.setColumnIdentifiers(headers);
            int expectedLength = headers.length;

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Ensure data length matches expected header columns
                if (data.length != expectedLength) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                model.addRow(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }
    
    public static List<String> ReadFileAsList(Class<?> classObj) {
        String fileName = classObj.getSimpleName() + ".txt";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    
    // Method to build a map of ItemID to ItemName from Item.txt
    public static Map<String, String> getItemMap() {
        Map<String, String> itemMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                itemMap.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemMap;
    }
    
    public void deleteItemFromAllFile(String itemID, String prFilePath) {
        List<String> updatedPRLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(prFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String currentItemID = parts[1].trim(); // Adjust index if itemID is at a different position
                if (!currentItemID.equals(itemID)) {
                    updatedPRLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Write the updated lines back to the PR file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prFilePath))) {
            for (String updatedLine : updatedPRLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean deleteWithDependencies(String id, boolean isPR) {
        PRManager prManager = new PRManager();
        POManager poManager = new POManager();

        List<PurchaseRequisition> prList = prManager.load();
        List<PurchaseOrder> poList = poManager.load();

        boolean success = false;

        if (isPR) {
            // Delete PR and its linked PO(s)
            Iterator<PurchaseRequisition> prIterator = prList.iterator();
            while (prIterator.hasNext()) {
                PurchaseRequisition pr = prIterator.next();
                if (pr.getPrID().equals(id)) {
                    prIterator.remove();
                    success = true;

                    // Delete any linked PO(s)
                    poList.removeIf(po -> po.getPrID().equals(id));
                    break;
                }
            }
        } else {
            // Delete PO and change PR status to "Pending"
            String relatedPRID = null;
            Iterator<PurchaseOrder> poIterator = poList.iterator();
            while (poIterator.hasNext()) {
                PurchaseOrder po = poIterator.next();
                if (po.getPoID().equals(id)) {
                    relatedPRID = po.getPrID();
                    poIterator.remove();
                    success = true;
                    break;
                }
            }

            if (relatedPRID != null) {
                for (PurchaseRequisition pr : prList) {
                    if (pr.getPrID().equals(relatedPRID)) {
                        pr.setStatus("Pending");
                        break;
                    }
                }
            }
        }

        // Save the updated lists
        boolean prSaved = prManager.saveAll(prList);
        boolean poSaved = poManager.saveAll(poList);

        return success && prSaved && poSaved;
    }
}
