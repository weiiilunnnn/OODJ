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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PDF Generator for Stock Reports.
 */
public class StockPdfGenerator extends PdfGenerator {

    private final List<String> headers;
    private final List<List<String>> data;

    public StockPdfGenerator(String outputPath, boolean previewAfterSave, List<String> headers, List<List<String>> data) {
        super(outputPath, previewAfterSave);
        this.headers = headers;
        this.data = data;
    }

    @Override
    protected void addTitle(Document document) throws DocumentException {
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph titlePara = new Paragraph("Stock Report", titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        titlePara.setSpacingAfter(20f);
        document.add(titlePara);
    }

    @Override
    protected void addTimestamp() {
        try {
            Font timeFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph timestamp = new Paragraph("Generated on: " + new java.util.Date(), timeFont);
            timestamp.setAlignment(Element.ALIGN_RIGHT);
            timestamp.setSpacingAfter(10f);
            document.add(timestamp);
        } catch (Exception e) {
            Logger.getLogger(StockPdfGenerator.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    protected void addContent(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(headers.size());
        table.setWidthPercentage(100);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            table.addCell(cell);
        }

        for (List<String> row : data) {
            for (String cellData : row) {
                PdfPCell cell = new PdfPCell(new Phrase(cellData));
                cell.setPadding(5f);
                table.addCell(cell);
            }
        }

        table.setSpacingBefore(10f);
        document.add(table);
    }

    @Override
    protected void addFooter() {
        try {
            Paragraph footer = new Paragraph("Total Items: " + data.size(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
            footer.setAlignment(Element.ALIGN_LEFT);
            footer.setSpacingBefore(15f);
            document.add(footer);
        } catch (DocumentException ex) {
            Logger.getLogger(StockPdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
