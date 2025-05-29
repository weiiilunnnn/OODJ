/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.pdf;

/**
 *
 * @author shihongwong
 */

import models.ReceiptData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import models.PurchaseOrder;
import services.POManager;
import services.RecieptReportGenerator;
import java.io.FileOutputStream;

public class RecieptPdfGenerator {

    public void generateReceipt(String purchaseOrderId, File outputFile) {
        try {
            POManager poManager = new POManager();
            PurchaseOrder po = poManager.getById(purchaseOrderId);
            if (po == null) {
                System.out.println("Purchase order not found for ID: " + purchaseOrderId);
                return;
            }

            // Create parent directories if they don't exist
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // Create folders recursively
            }

            // Create the file if it does not exist
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            RecieptReportGenerator generator = new RecieptReportGenerator(po);
            ReceiptData data = generator.getReceiptData();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            // Add logo (fix your logo path accordingly)
            try {
                Image logo = Image.getInstance("/img/smallOSWB.png");
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception e) {
                // logo not added if missing
            }

            // Title
            Paragraph title = new Paragraph("Payment Receipt", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Payment details table
            PdfPTable paymentTable = new PdfPTable(2);
            paymentTable.setWidthPercentage(100);
            paymentTable.setSpacingAfter(10);
            paymentTable.addCell(getCell("Purchase Order ID:", PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell(data.getPurchaseOrderID(), PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell("Account Name:", PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell(data.getAccountName(), PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell("Account Number:", PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell(data.getAccountNumber(), PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell("Payment Time:", PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell(data.getTimestamp(), PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell("Paid By (User ID):", PdfPCell.ALIGN_LEFT));
            paymentTable.addCell(getCell(data.getPaidByUserID(), PdfPCell.ALIGN_LEFT));
            document.add(paymentTable);

            // Item info
            Paragraph itemTitle = new Paragraph("Item Information", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            itemTitle.setSpacingBefore(10);
            document.add(itemTitle);

            PdfPTable itemTable = new PdfPTable(2);
            itemTable.setWidthPercentage(100);
            itemTable.setSpacingAfter(10);
            itemTable.addCell(getCell("Item ID:", PdfPCell.ALIGN_LEFT));
            itemTable.addCell(getCell(data.getItemID(), PdfPCell.ALIGN_LEFT));
            itemTable.addCell(getCell("Item Name:", PdfPCell.ALIGN_LEFT));
            itemTable.addCell(getCell(data.getItemName(), PdfPCell.ALIGN_LEFT));
            itemTable.addCell(getCell("Quantity Ordered:", PdfPCell.ALIGN_LEFT));
            itemTable.addCell(getCell(String.valueOf(data.getQuantity()), PdfPCell.ALIGN_LEFT));
            document.add(itemTable);

            // Supplier info
            Paragraph suppTitle = new Paragraph("Supplier Information", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            suppTitle.setSpacingBefore(10);
            document.add(suppTitle);

            PdfPTable supplierTable = new PdfPTable(2);
            supplierTable.setWidthPercentage(100);
            supplierTable.setSpacingAfter(10);
            supplierTable.addCell(getCell("Supplier ID:", PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell(data.getSupplierID(), PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell("Supplier Name:", PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell(data.getSupplierName(), PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell("Supplier Address:", PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell(data.getSupplierAddress(), PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell("Supplier Contact No:", PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell(data.getSupplierContact(), PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell("Supplied Price:", PdfPCell.ALIGN_LEFT));
            supplierTable.addCell(getCell(String.format("RM %.2f", data.getItemPrice()), PdfPCell.ALIGN_LEFT));
            document.add(supplierTable);

            // Price summary
            Paragraph priceTitle = new Paragraph("Price Summary", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            priceTitle.setSpacingBefore(10);
            document.add(priceTitle);

            PdfPTable priceTable = new PdfPTable(2);
            priceTable.setWidthPercentage(100);
            priceTable.addCell(getCell("Subtotal:", PdfPCell.ALIGN_LEFT));
            priceTable.addCell(getCell(String.format("RM %.2f", data.getSubtotal()), PdfPCell.ALIGN_LEFT));
            priceTable.addCell(getCell("Tax (6%):", PdfPCell.ALIGN_LEFT));
            priceTable.addCell(getCell(String.format("RM %.2f", data.getTax()), PdfPCell.ALIGN_LEFT));
            priceTable.addCell(getCell("Total:", PdfPCell.ALIGN_LEFT));
            priceTable.addCell(getCell(String.format("RM %.2f", data.getTotal()), PdfPCell.ALIGN_LEFT));
            document.add(priceTable);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
