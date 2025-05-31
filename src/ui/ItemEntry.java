/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import models.User;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.Admin;
import models.InventoryManager;
import models.Item;
import models.PurchaseManager;
import models.SalesManager;
import services.FileOperation;
import services.IDGenerator;
import services.ItemManager;

/**
 *
 * @author lunwe
 */
public class ItemEntry extends javax.swing.JFrame {
    private User user;
    private ItemManager manager = new ItemManager();
    /** Data model (list of Item objects) */
    private List<Item> itemList = new ArrayList<>();
    /** Table model and sorter for persistent sorting and filtering */
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form ItemEntry
     */
    public ItemEntry(User user) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        
        // Disable editing buttons for certain roles
        if (user instanceof PurchaseManager || user instanceof InventoryManager) {
            btnAddItem.setVisible(false);
            btnUpdateItem.setVisible(false);
            btnDeleteItem.setVisible(false);
            btnClearForm.setVisible(false);
        }
        
        itemList = manager.load();
        tableModel = manager.getItemTableModel();
        tblItems.setModel(tableModel);
        String itemID = IDGenerator.generateNextID("ITM", "Item.txt");
        txtItemID.setText(itemID);

        sorter = new TableRowSorter<>(tableModel);
        tblItems.setRowSorter(sorter);
        sorter.setComparator(2, (o1, o2) -> Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString())));
        sorter.setComparator(3, (o1, o2) -> Double.compare(Double.parseDouble(o1.toString()), Double.parseDouble(o2.toString())));
        sorter.setComparator(4, (o1, o2) -> Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString())));

        /** Row selection auto fill the form fields for editing */
        tblItems.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblItems.getSelectedRow();
                if (row >= 0 && tblItems.getRowCount() > 0 && tblItems.getColumnCount() >= 5) {
                    try {
                        txtItemID.setText(tblItems.getValueAt(row, 0).toString());
                        txtItemName.setText(tblItems.getValueAt(row, 1).toString());
                        txtItemQty.setText(tblItems.getValueAt(row, 2).toString());
                        txtItemPrice.setText(tblItems.getValueAt(row, 3).toString());
                        txtReorderLevel.setText(tblItems.getValueAt(row, 4).toString());

                        int qty = Integer.parseInt(tblItems.getValueAt(row, 2).toString());
                        int rol = Integer.parseInt(tblItems.getValueAt(row, 4).toString());
                        txtFlagStatus.setText((qty > rol) ? "OK" : "Restock");

                        btnAddItem.setEnabled(false);
                    } catch (Exception ex) {
                        System.err.println("Error reading selected row: " + ex.getMessage());
                    }
                }
            }
        });

        /** Real-time search: filter table as user types */
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String keyword = txtSearch.getText().trim();
                filterItems(keyword);
            }
        });
    }
    
    /** Capitalizes each word in the item name before saving/displaying */
    private String capitalizeEachWord(String input) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                           .append(word.substring(1)).append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    /** Validates user inputs before add/update */
    private boolean validateInputs() {
        String itemName = txtItemName.getText().trim();
        if (itemName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item name cannot be empty.");
            return false;
        }
        if (!itemName.matches(".*[a-zA-Z].*")) {
            JOptionPane.showMessageDialog(this, "Item name must contain at least one letter.");
            return false;
        }
        if (itemName.length() < 3) {
            JOptionPane.showMessageDialog(this, "Item name must be at least 3 characters long.");
            return false;
        }
        try {
            int qty = Integer.parseInt(txtItemQty.getText().trim());
            if (qty < 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be 0 or more.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be an integer.");
            return false;
        }
        try {
            double price = Double.parseDouble(txtItemPrice.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price must be 0 or more.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a number.");
            return false;
        }
        try {
            int rol = Integer.parseInt(txtReorderLevel.getText().trim());
            if (rol < 0) {
                JOptionPane.showMessageDialog(this, "Reorder Level must be 0 or more.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Reorder Level must be an integer.");
            return false;
        }
        return true;
    }

    /** Filters the table rows using the sorter instead of replacing the model */
    private void filterItems(String keyword) {
        keyword = keyword.toLowerCase();
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null); // clear filter
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + keyword, 0, 1)); // ID and Name columns
        }
    }
    
    private void setEmpty(){
        txtItemName.setText("");
        txtItemQty.setText("");
        txtItemPrice.setText("");
        txtReorderLevel.setText("");
        txtFlagStatus.setText("");
        String itemID = IDGenerator.generateNextID("ITM", "Item.txt");
        txtItemID.setText(itemID);
        btnAddItem.setEnabled(true); // re-enable Add
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ItemPriceLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        ROLLabel = new javax.swing.JLabel();
        btnAddItem = new javax.swing.JButton();
        txtItemID = new javax.swing.JTextField();
        btnUpdateItem = new javax.swing.JButton();
        txtItemName = new javax.swing.JTextField();
        btnDeleteItem = new javax.swing.JButton();
        txtItemQty = new javax.swing.JTextField();
        btnClearForm = new javax.swing.JButton();
        txtItemPrice = new javax.swing.JTextField();
        txtFlagStatus = new javax.swing.JTextField();
        txtReorderLevel = new javax.swing.JTextField();
        FlagStatusLabel = new javax.swing.JLabel();
        ItemIDLabel = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        ItemNameLabel = new javax.swing.JLabel();
        ItemQtyLabel = new javax.swing.JLabel();
        ItemNameLabel1 = new javax.swing.JLabel();
        btnBack1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel14.setText("jLabel14");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Item Entry");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(8, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        ItemPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemPriceLabel.setText("Sales Price:");

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblItems);

        ROLLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ROLLabel.setText("Reorder Level:");

        btnAddItem.setBackground(new java.awt.Color(102, 255, 102));
        btnAddItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddItem.setText("Add");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        txtItemID.setEnabled(false);

        btnUpdateItem.setBackground(new java.awt.Color(51, 204, 255));
        btnUpdateItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateItem.setText("Update");
        btnUpdateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateItemActionPerformed(evt);
            }
        });

        btnDeleteItem.setBackground(new java.awt.Color(255, 51, 51));
        btnDeleteItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteItem.setText("Delete");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        btnClearForm.setBackground(new java.awt.Color(255, 153, 51));
        btnClearForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearForm.setText("Cancel");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });

        txtFlagStatus.setColumns(5);
        txtFlagStatus.setEnabled(false);

        FlagStatusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FlagStatusLabel.setText("Flag Status:");

        ItemIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemIDLabel.setText("Item ID:");

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel.setText("Item Name:");

        ItemQtyLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemQtyLabel.setText("Item Quantity:");

        ItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel1.setText("Search Item:");

        btnBack1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack1.setText("Back");
        btnBack1.setPreferredSize(new java.awt.Dimension(82, 23));
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ItemNameLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnUpdateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(btnClearForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ItemIDLabel)
                                            .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ItemNameLabel)
                                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtItemQty, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ItemQtyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ItemPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ROLLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtReorderLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(FlagStatusLabel)
                                            .addComponent(txtFlagStatus)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(152, 152, 152)
                                        .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(42, 42, 42))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemNameLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(ItemIDLabel)
                                    .addGap(5, 5, 5)
                                    .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(ItemNameLabel)
                                    .addGap(5, 5, 5)
                                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(FlagStatusLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtFlagStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnBack1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(16, 16, 16))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ItemPriceLabel)
                                .addComponent(ROLLabel))
                            .addGap(5, 5, 5)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtReorderLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(74, 74, 74)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ItemQtyLabel)
                        .addGap(5, 5, 5)
                        .addComponent(txtItemQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        if (user instanceof PurchaseManager || user instanceof InventoryManager) {
            btnAddItem.setEnabled(false);
            btnUpdateItem.setEnabled(false);
            btnDeleteItem.setEnabled(false);
        }
        
        if (!validateInputs()) return;

        String id = IDGenerator.generateNextID("ITM","Item.txt");
        String name = capitalizeEachWord(txtItemName.getText().trim());
        txtItemName.setText(name);
        int qty = Integer.parseInt(txtItemQty.getText().trim());
        double price = Double.parseDouble(txtItemPrice.getText().trim());
        int rol = Integer.parseInt(txtReorderLevel.getText().trim());

        Item newItem = new Item(id, name, qty, price, rol);
        manager.add(newItem, itemList);
        // Add row to existing tableModel instead of resetting it
        tableModel.addRow(new Object[] {
            newItem.getItemID(),
            newItem.getItemName(),
            newItem.getItemQty(),
            newItem.getItemPrice(),
            newItem.getReorderLevel(),
            newItem.getFlagStatus()
        });

        JOptionPane.showMessageDialog(this, "Item added successfully.");
        btnClearForm.doClick();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnUpdateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateItemActionPerformed
        if (user instanceof PurchaseManager || user instanceof InventoryManager) {
            btnAddItem.setEnabled(false);
            btnUpdateItem.setEnabled(false);
            btnDeleteItem.setEnabled(false);
        }
        
        int selectedRow = tblItems.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
            return;
        }

        if (!validateInputs()) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to update this item?",
            "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String id = txtItemID.getText();
            String name = capitalizeEachWord(txtItemName.getText().trim());
            txtItemName.setText(name);
            int qty = Integer.parseInt(txtItemQty.getText().trim());
            double price = Double.parseDouble(txtItemPrice.getText().trim());
            int rol = Integer.parseInt(txtReorderLevel.getText().trim());

            Item updatedItem = new Item(id, name, qty, price, rol);
            manager.update(updatedItem, itemList);
            tblItems.setModel(manager.getItemTableModel());

            JOptionPane.showMessageDialog(this, "Item updated successfully.");
            btnClearForm.doClick();
        } else {
            JOptionPane.showMessageDialog(this, "Update cancelled.");
        }
    }//GEN-LAST:event_btnUpdateItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        if (user instanceof PurchaseManager || user instanceof InventoryManager) {
            btnAddItem.setEnabled(false);
            btnUpdateItem.setEnabled(false);
            btnDeleteItem.setEnabled(false);
        }        
        
        int selectedRow = tblItems.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        String itemId = tblItems.getValueAt(selectedRow, 0).toString(); // Item ID column
        String itemName = tblItems.getValueAt(selectedRow, 1).toString(); // Item Name column

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete item \"" + itemName + "\" (ID: " + itemId + ")?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.delete(itemId, itemList); // Assumes itemList is the list used to populate the table
            FileOperation fo = new FileOperation();
            fo.deleteItemFromAllFile(itemId, "PurchaseRequisition.txt");
            fo.deleteItemFromAllFile(itemId, "PurchaseOrder.txt");
            fo.deleteItemFromAllFile(itemId, "SupplierList.txt");

            // Refresh the table model
            DefaultTableModel model = manager.getItemTableModel();
            tblItems.setModel(model);

            // Optional: set row sorter if needed (e.g. for sorting quantities/prices)
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tblItems.setRowSorter(sorter);

            // Example: set numeric column comparator for quantity column (column index 2)
            sorter.setComparator(2, (o1, o2) -> Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString())));

            JOptionPane.showMessageDialog(this, "Item deleted successfully.");

            // Clear form inputs
            setEmpty(); // Create this method or put clear code here

        } else {
            JOptionPane.showMessageDialog(this, "Deletion cancelled.");
        }
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
        setEmpty();
        
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        this.dispose();

        if (user instanceof Admin) {
            new AdminMenu((Admin) user).setVisible(true);
        } else if (user instanceof PurchaseManager) {
            new PurchaseManagerMenu((PurchaseManager) user).setVisible(true);
        } else if (user instanceof SalesManager) {
            new SalesManagerMenu((SalesManager) user).setVisible(true);
        } else if (user instanceof InventoryManager) {
            new InventoryManagerMenu((InventoryManager) user).setVisible(true);
        } else {
            // Optional: handle unknown user type
            JOptionPane.showMessageDialog(null, "Unknown user type. Cannot navigate back.");
        }
    }//GEN-LAST:event_btnBack1ActionPerformed

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
            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ItemEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FlagStatusLabel;
    private javax.swing.JLabel ItemIDLabel;
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JLabel ItemNameLabel1;
    private javax.swing.JLabel ItemPriceLabel;
    private javax.swing.JLabel ItemQtyLabel;
    private javax.swing.JLabel ROLLabel;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnUpdateItem;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtFlagStatus;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrice;
    private javax.swing.JTextField txtItemQty;
    private javax.swing.JTextField txtReorderLevel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
