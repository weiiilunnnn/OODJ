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
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.PurchaseRequisition;

/**
 *
 * @author lunwe
 */
public class FileOperation {
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
    
    
}
