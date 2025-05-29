/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author shihongwong
 */

import models.Item;
import services.FileOperation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JOptionPane;
import services.pdf.StockPdfGenerator;

public final class StockReportGenerator {

    private StockReportGenerator() {}

    public static void generateStockReport(File outputFile, boolean preview) throws IOException, Exception {
        List<String> headers = Arrays.asList("Item ID", "Item Name", "Quantity", "Price");
        List<List<String>> data = getStockData();

        StockPdfGenerator pdfGenerator = new StockPdfGenerator(outputFile.getAbsolutePath(), preview, headers, data);
        pdfGenerator.generate();
    }

    public static File getDefaultFilePath() {
        String fileName = "Stock_Report_" + LocalDate.now() + ".pdf";
        return new File(fileName);
    }

    private static List<List<String>> getStockData() {
        List<List<String>> data = new ArrayList<>();

        try {
            List<String> lines = FileOperation.ReadFileAsList(Item.class);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    data.add(Arrays.asList(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading stock data or generating report.");
            e.printStackTrace();
        }

        return data;
    }
}
