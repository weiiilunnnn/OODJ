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
import java.io.FileOutputStream;
import java.io.File;

public abstract class PdfGenerator {
    protected final String outputPath;
    protected final boolean previewAfterSave;
    protected Document document;

    public PdfGenerator(String outputPath, boolean previewAfterSave) {
        this.outputPath = outputPath;
        this.previewAfterSave = previewAfterSave;
    }

    public void generate() {
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            addLogo();
            addTitle(document);
            addTimestamp();
            addContent(document);
            addFooter();

            document.close();

            if (previewAfterSave) {
                // open file
                java.awt.Desktop.getDesktop().open(new File(outputPath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addLogo() throws DocumentException {
        // Optional
    }

    protected abstract void addTitle(Document document) throws DocumentException;

    protected abstract void addTimestamp();

    protected abstract void addContent(Document document) throws DocumentException;

    protected abstract void addFooter();
}
