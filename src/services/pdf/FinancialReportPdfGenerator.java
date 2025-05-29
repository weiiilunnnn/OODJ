/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.pdf;

/**
 *
 * @author shihongwong
 */
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import services.FinancialReportGenerator;
import services.FinancialReportGenerator.ItemSummary;

import javax.swing.*;
import java.io.FileOutputStream;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class FinancialReportPdfGenerator {

    public boolean generateReport(int month, int year, File outputPath) {
        try {
            FinancialReportGenerator generator = new FinancialReportGenerator();
            Map<String, ItemSummary> itemSummaries = generator.generateReportData(month, year);

            if (itemSummaries == null || itemSummaries.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No financial data found for " + Month.of(month) + " " + year + ".",
                        "No Data", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            addTitle(document, month, year);
            addSummary(document, itemSummaries);
            addItemTable(document, itemSummaries);

            document.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error generating financial report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }


    private void addTitle(Document document, int month, int year) throws DocumentException {
        String monthName = Month.of(month).name();
        Paragraph title = new Paragraph("Financial Report - " + capitalize(monthName) + " " + year,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addSummary(Document document, Map<String, ItemSummary> summaryMap) throws DocumentException {
        int totalItems = summaryMap.size();
        int totalQuantity = summaryMap.values().stream().mapToInt(ItemSummary::getTotalQuantity).sum();
        double totalCost = summaryMap.values().stream().mapToDouble(ItemSummary::getTotalCost).sum();

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidths(new int[]{2, 4});
        summaryTable.setWidthPercentage(60);
        summaryTable.setSpacingAfter(20);
        summaryTable.setHorizontalAlignment(Element.ALIGN_CENTER);

        addSummaryRow(summaryTable, "Total Unique Items:", String.valueOf(totalItems));
        addSummaryRow(summaryTable, "Total Quantity Ordered:", String.valueOf(totalQuantity));
        addSummaryRow(summaryTable, "Total Expenditure (RM):", String.format("%.2f", totalCost));

        document.add(summaryTable);
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont));

        labelCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addItemTable(Document document, Map<String, ItemSummary> summaryMap) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[]{2, 5, 3, 4});
        table.setWidthPercentage(90);
        table.setSpacingBefore(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        addTableHeader(table, headerFont, "Item ID", "Item Name", "Quantity", "Total Cost (RM)");

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        for (ItemSummary summary : summaryMap.values()) {
            table.addCell(new PdfPCell(new Phrase(summary.getItemID(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(summary.getItemName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(summary.getTotalQuantity()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", summary.getTotalCost()), cellFont)));
        }

        document.add(table);
    }

    private void addTableHeader(PdfPTable table, Font font, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, font));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
    }

    private String capitalize(String word) {
        return word.charAt(0) + word.substring(1).toLowerCase();
    }
}
