/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import models.Admin;
import models.FinanceManager;
import models.Item;
import models.PurchaseManager;
import models.SalesManager;
import models.User;
import services.FileOperation;
import services.ItemManager;

/**
 *
 * @author shihongwong
 */
public class DailySalesEntry extends javax.swing.JFrame {
    private User user;
    private ItemManager itemManager = new ItemManager();
    private int Total;
    private String originalItemId = "";
    private String originalItemName = "";
    private String originalSalesQty = "";


    public DailySalesEntry(User user) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        setupTextFieldListeners();
        setupTableSelectionListener();
    }
    
    private void updateItemNameBasedOnId() {
        String itemId = ItemIdTf.getText().trim();

        if (!itemId.isEmpty()) {
            try {
                Item item = itemManager.findItemById(itemId);
                if (item != null) {
                    String itemName = item.getItemName();
                    System.out.println("Setting item name: " + itemName);
                    SwingUtilities.invokeLater(() -> {
                        ItemNameTf.setText(itemName);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        ItemNameTf.setText("");
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    ItemNameTf.setText("");
                });
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                ItemNameTf.setText("");
            });
        }
    }

    
    private void updateAddButtonState() {
        String itemId = ItemIdTf.getText().trim();
        String itemName = ItemNameTf.getText().trim();
        String quantityStr = SalesQuantityTf.getText().trim();
        Item item = itemManager.findItemById(itemId);

        boolean isFilled = !itemId.isEmpty() && !itemName.isEmpty() && !quantityStr.isEmpty();
        boolean notInTable = true;
        DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 0);
            if (val != null && val.toString().equalsIgnoreCase(itemId)) {
                notInTable = false;
                break;
            }
        }

        boolean itemAvailability = false;
        try {
            int enteredQuantity = Integer.parseInt(quantityStr);
            if (item != null) {
                int itemQuantity = item.getItemQty();
                itemAvailability = itemQuantity >= enteredQuantity;
            }
        } catch (NumberFormatException e) {
            // Invalid number entered, treat as no availability
            itemAvailability = false;
        }

        if (isFilled && notInTable && itemAvailability) {
            AddButton.setEnabled(true);
            AddButton.setBackground(Color.GREEN);
        } else {
            AddButton.setEnabled(false);
            AddButton.setBackground(Color.GRAY);
        }
    }

    
    private void updateClearButtonState() {
        boolean hasText = !ItemIdTf.getText().trim().isEmpty()
                || !ItemNameTf.getText().trim().isEmpty()
                || !SalesQuantityTf.getText().trim().isEmpty();
        if (hasText) {
            ClearButton.setEnabled(true);
            ClearButton.setBackground(Color.GREEN);
        } else {
            ClearButton.setEnabled(false);
            ClearButton.setBackground(Color.GRAY);
        }
        
    }
    
    private void clearFields() {
        ItemIdTf.setText("");
        ItemNameTf.setText("");
        SalesQuantityTf.setText("");
        SalesTable.clearSelection();
    }
    
    private void setupTableSelectionListener() {
        SalesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = SalesTable.getSelectedRow();
                if (selectedRow != -1) {

                    Object objItemId = SalesTable.getValueAt(selectedRow, 0);
                    Object objItemName = SalesTable.getValueAt(selectedRow, 1);
                    Object objSalesQty = SalesTable.getValueAt(selectedRow, 2);

                    String itemId = objItemId != null ? objItemId.toString() : "";
                    String itemName = objItemName != null ? objItemName.toString() : "";
                    String salesQty = objSalesQty != null ? objSalesQty.toString() : "";

                    ItemIdTf.setText(itemId);
                    ItemNameTf.setText(itemName);
                    SalesQuantityTf.setText(salesQty);
                    
                    originalItemId = itemId;
                    originalItemName = itemName;
                    originalSalesQty = salesQty;

                    updateAddButtonState();
                    updateClearButtonState();
                    checkUpdateButtonState();
                    updateSubmitButtonState();


                    DeleteButton.setEnabled(true);
                    DeleteButton.setBackground(Color.RED);
                } else {
                    DeleteButton.setEnabled(false);
                    DeleteButton.setBackground(Color.GRAY);
                }
            }
        });
    }
    
    private void checkUpdateButtonState() {
        String currentItemId = ItemIdTf.getText().trim();
        String currentItemName = ItemNameTf.getText().trim();
        String currentSalesQty = SalesQuantityTf.getText().trim();

        boolean allFilled = !currentItemId.isEmpty() && !currentItemName.isEmpty() && !currentSalesQty.isEmpty();

        // Validate quantity is a positive integer
        boolean isQuantityValid = false;
        try {
            int qty = Integer.parseInt(currentSalesQty);
            isQuantityValid = qty > 0;
        } catch (NumberFormatException e) {
            isQuantityValid = false;
        }

        // Check if current itemId exists in the sales table
        boolean isItemIdInTable = false;
        DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 0);
            if (val != null && val.toString().equalsIgnoreCase(currentItemId)) {
                isItemIdInTable = true;
                break;
            }
        }

        // Check if any of the values have changed compared to original
        boolean isChanged = !currentItemId.equalsIgnoreCase(originalItemId)
                         || !currentItemName.equals(originalItemName)
                         || !currentSalesQty.equals(originalSalesQty);

        if (allFilled && isQuantityValid && isChanged && isItemIdInTable) {
            UpdateButton.setEnabled(true);
            UpdateButton.setBackground(Color.GREEN);
        } else {
            UpdateButton.setEnabled(false);
            UpdateButton.setBackground(Color.GRAY);
        }
    }

    
    private void setupTextFieldListeners() {
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateButtons(); }
            public void removeUpdate(DocumentEvent e) { updateButtons(); }
            public void changedUpdate(DocumentEvent e) { updateButtons(); }

            private void updateButtons() {
                updateItemNameBasedOnId();
                updateAddButtonState();
                updateClearButtonState();
                checkUpdateButtonState();
            }
        };
        
        ItemIdTf.getDocument().addDocumentListener(docListener);
        SalesQuantityTf.getDocument().addDocumentListener(docListener);
    }


    private void updateSubmitButtonState() {
        DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();
        if(model.getRowCount() > 0){
            SubmitButton.setEnabled(true);
            SubmitButton.setBackground(Color.white);
        } else{
            SubmitButton.setEnabled(false);
            SubmitButton.setBackground(Color.GRAY);
        }
    }


    //-------------------
    

        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel14 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        BackBtn = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        ItemIdTf = new javax.swing.JTextField();
        Scroll = new javax.swing.JScrollPane();
        SalesTable = new javax.swing.JTable();
        ItemNameTf = new javax.swing.JTextField();
        SalesQuantityTf = new javax.swing.JTextField();
        AddButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        StockReport1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DeleteButton = new javax.swing.JButton();
        ClearButton = new javax.swing.JButton();
        SubmitButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        UpdateButton = new javax.swing.JButton();

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel14.setText("jLabel14");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(880, 555));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        BackBtn.setText("Back");
        BackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtnActionPerformed(evt);
            }
        });

        jLabel26.setText("Item ID:");

        jLabel27.setText("Item Name:");

        jLabel28.setText("Sales Quantity:");

        SalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Item ID", "Item Name", "Quantity Sales"
            }
        ));
        Scroll.setViewportView(SalesTable);

        ItemNameTf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemNameTfActionPerformed(evt);
            }
        });

        AddButton.setText("Add");
        AddButton.setEnabled(false);
        AddButton.setBackground(Color.GRAY);
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 786));

        StockReport1.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        StockReport1.setForeground(new java.awt.Color(255, 255, 255));
        StockReport1.setText("Daily Sales Entry");

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel31.setText("jLabel14");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(StockReport1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(StockReport1)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setText("Sales Entry");

        jLabel2.setText(LocalDate.now().toString());

        DeleteButton.setText("Delete");
        DeleteButton.setEnabled(false);
        DeleteButton.setBackground(Color.GRAY);
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        ClearButton.setText("Clear");
        ClearButton.setEnabled(false);
        ClearButton.setBackground(Color.GRAY);
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        SubmitButton.setText("Submit");
        SubmitButton.setEnabled(false);
        SubmitButton.setBackground(Color.GRAY);
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });

        UpdateButton.setText("Update");
        UpdateButton.setEnabled(false);
        UpdateButton.setBackground(Color.GRAY);
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        ItemNameTf.setEditable(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(BackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(195, 195, 195)
                                .addComponent(jLabel4)
                                .addGap(0, 239, Short.MAX_VALUE)))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(jLabel2))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(jLabel1))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel28)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(32, 32, 32)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ItemIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ItemNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(SalesQuantityTf, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(41, 41, 41))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addComponent(AddButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(DeleteButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(ClearButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(UpdateButton)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(ItemIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ItemNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(SalesQuantityTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AddButton)
                            .addComponent(DeleteButton)
                            .addComponent(ClearButton)
                            .addComponent(UpdateButton))
                        .addGap(36, 36, 36)
                        .addComponent(jLabel4)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtnActionPerformed
        this.dispose();

        if (user instanceof SalesManager) {
            new SalesManagerMenu((SalesManager) user).setVisible(true);
        } else if (user instanceof Admin) {
            new AdminMenu((Admin) user).setVisible(true);
        } else {
            // Optional: handle unknown user type
            JOptionPane.showMessageDialog(null, "Unknown user type. Cannot navigate back.");
        }
    }//GEN-LAST:event_BackBtnActionPerformed

    private void ItemNameTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemNameTfActionPerformed

    }//GEN-LAST:event_ItemNameTfActionPerformed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        String itemId = ItemIdTf.getText().trim();
        String itemName = ItemNameTf.getText().trim();
        String quantityStr = SalesQuantityTf.getText().trim();
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new NumberFormatException();

            Item item = itemManager.findItemById(itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Invalid Item ID.");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();
            model.addRow(new Object[]{itemId.toUpperCase(), itemName, quantity});
            clearFields();
            
            updateSubmitButtonState();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.");
        }
    }//GEN-LAST:event_AddButtonActionPerformed

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitButtonActionPerformed
         DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String itemId = model.getValueAt(i, 0).toString();
            int soldQty = Integer.parseInt(model.getValueAt(i, 2).toString());

            Item item = itemManager.findItemById(itemId);
            if (item != null) {
                int updatedQty = item.getItemQty() - soldQty;
                itemManager.updateQuantity(itemId, updatedQty);
                model.setRowCount(0);
                JOptionPane.showMessageDialog(null, "Sales submitted successfully. Inventory updated.");
            }
        }
        
        boolean lowStockFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader("Item.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[5].trim().equalsIgnoreCase("Restock")) {
                    lowStockFound = true;
                    break; // stop at first restock found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading stock file.");
            return;
        }

        // If restock needed, prompt user
        if (lowStockFound) {
            int choice = JOptionPane.showConfirmDialog(null, "Item on low stock. Want to create PR?", "Low Stock Alert", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new CreatePR(user).setVisible(true);
            } else {
                SalesManagerMenu sm = new SalesManagerMenu(user);
                this.dispose();
                sm.setVisible(true);
            }
        }
        

    }//GEN-LAST:event_SubmitButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        int selectedRow = SalesTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();
            model.removeRow(selectedRow);
            clearFields();
            DeleteButton.setEnabled(false);
            DeleteButton.setBackground(Color.GRAY);
            updateSubmitButtonState();
        }
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        clearFields();
        DeleteButton.setEnabled(false);
        DeleteButton.setBackground(Color.GRAY);
        updateAddButtonState();
        updateClearButtonState();
        checkUpdateButtonState();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        int selectedRow = SalesTable.getSelectedRow();
        if (selectedRow != -1) {
            String itemId = ItemIdTf.getText().trim();
            String itemName = ItemNameTf.getText().trim();
            String quantityStr = SalesQuantityTf.getText().trim();

            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) throw new NumberFormatException();

                DefaultTableModel model = (DefaultTableModel) SalesTable.getModel();

                // Update only if item exists in the row
                model.setValueAt(itemId, selectedRow, 0);
                model.setValueAt(itemName, selectedRow, 1);
                model.setValueAt(quantity, selectedRow, 2);

                // Reset original values to the updated ones
                originalItemId = itemId;
                originalItemName = itemName;
                originalSalesQty = quantityStr;

                clearFields();
                SalesTable.clearSelection();

                UpdateButton.setEnabled(false);
                UpdateButton.setBackground(Color.GRAY);

                DeleteButton.setEnabled(false);
                DeleteButton.setBackground(Color.GRAY);

                updateSubmitButtonState();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.");
            }
        }
    }//GEN-LAST:event_UpdateButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton BackBtn;
    private javax.swing.JButton ClearButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JTextField ItemIdTf;
    private javax.swing.JTextField ItemNameTf;
    private javax.swing.JTextField SalesQuantityTf;
    private javax.swing.JTable SalesTable;
    private javax.swing.JScrollPane Scroll;
    private javax.swing.JLabel StockReport1;
    private javax.swing.JButton SubmitButton;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
