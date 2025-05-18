/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.io.*;
/**
 *
 * @author lunwe
 */
public class IDGenerator {
    public static String generateNextID(String prefix, String fileName) {
        int max = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(prefix)) {
                    String[] parts = line.split(",");
                    String id = parts[0]; // assuming ID is the first field
                    int num = Integer.parseInt(id.replace(prefix, ""));
                    if (num > max) max = num;
                }
            }
        } catch (IOException e) {
            // File might not exist yet; that's okay
        }

        return prefix + String.format("%03d", max + 1);
    }
}
