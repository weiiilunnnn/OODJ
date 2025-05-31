/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.PurchaseManager;
import models.PurchaseOrder;
import models.User;
import services.POManager;
import java.util.Map;
import models.Admin;
import models.FinanceManager;
import models.InventoryManager;
import models.SalesManager;
import services.FileOperation;
import services.ValidateInputs;



public class ViewPO extends javax.swing.JFrame {
    private User user;
    private POManager manager = new POManager();
    private List<PurchaseOrder> POList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private Map<String, String> itemMap;
     
     
    public ViewPO(User user, String poID, String prID, String itemID, String itemName, String quantity, String supplierID, String supplierName, String distance, String price, boolean isUpdate) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        
        if (user instanceof SalesManager || user instanceof FinanceManager || user instanceof InventoryManager) {
            btnUpdatePO.setVisible(false);
            btnDeletePO.setVisible(false);
            btnClearForm.setVisible(false);
            btnCreateNew.setVisible(false);
        }
        
        txtPOID.setText(poID);
        txtPRID.setText(prID);
        txtItemID.setText(itemID);
        txtItemName.setText(itemName);
        txtRestockQty.setText(quantity);
        txtSupplierID.setText(supplierID);
        btnUpdatePO.setEnabled(false);
        

        // Load PO list and table model
        POList = manager.load();
        tableModel = manager.getPOTableModel();
        tablePO.setModel(tableModel);

        // Load item ID to name map
        itemMap = FileOperation.getItemMap();

        // Set up table row sorter
        sorter = new TableRowSorter<>(tableModel);
        tablePO.setRowSorter(sorter);

        // Sort for quantity and date
        sorter.setComparator(4, Comparator.comparingDouble(o -> Double.parseDouble(o.toString())));
        sorter.setComparator(6, (o1, o2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(o1.toString()).compareTo(sdf.parse(o2.toString()));
            } catch (Exception e) {
                return 0;
            }
        });
        
        // Handle row selection
        tablePO.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablePO.getSelectedRow();
                if (row >= 0 && tablePO.getRowCount() > 0 && tablePO.getColumnCount() >= 9) {
                    try {
                        txtPOID.setText(tablePO.getValueAt(row, 0).toString());
                        txtPRID.setText(tablePO.getValueAt(row, 1).toString());

                        String itemId = tablePO.getValueAt(row, 2).toString();
                        txtItemID.setText(itemId);
                        txtItemName.setText(itemMap.getOrDefault(itemId, "Unknown Item"));

                        txtRestockQty.setText(tablePO.getValueAt(row, 4).toString());

                        String dateStr = tablePO.getValueAt(row, 6).toString();
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = formatter.parse(dateStr);
                            txtPurchaseDate.setDate(date);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Failed to parse date: " + ex.getMessage());
                        }

                        txtStatus.setText(tablePO.getValueAt(row, 7).toString());
                        txtSupplierID.setText(tablePO.getValueAt(row, 8).toString());
                        txtSuppliedPrice.setText(tablePO.getValueAt(row, 9).toString());
                        btnUpdatePO.setEnabled(true);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error loading PO details: " + ex.getMessage());
                    }
                }
            }
        });
    
        
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String keyword = txtSearch.getText().trim();
                filterItems(keyword);
            }
        });
    }
    
    public ViewPO(User user) {
        this(user, "", "", "", "", "", "", "", "", "", false);
    }

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
        return ValidateInputs.validateTodayOrFutureDate(
            txtPurchaseDate.getDate(),
            this,
            "Required Date"
        );
    }

    /** Filters the table rows using the sorter instead of replacing the model */
    private void filterItems(String keyword) {
        keyword = keyword.toLowerCase();
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + keyword, 0, 1, 2, 3, 5, 7));
        }
    }
    
    private void clearPOForm() {
        txtPOID.setText("");
        txtPRID.setText("");
        txtItemName.setText("");
        txtRestockQty.setText("");
        txtItemID.setText("");
        txtStatus.setText("");
        txtPurchaseDate.setDate(null); 
        txtSupplierID.setText("");
        txtSuppliedPrice.setText("");
        btnUpdatePO.setEnabled(false);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ItemQtyLabel = new javax.swing.JLabel();
        btnCreateNew = new javax.swing.JButton();
        btnDeletePO = new javax.swing.JButton();
        ItemIDLabel = new javax.swing.JLabel();
        ItemNameLabel1 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        btnBack1 = new javax.swing.JButton();
        txtItemName = new javax.swing.JTextField();
        btnClearForm = new javax.swing.JButton();
        txtPurchaseDate = new com.toedter.calendar.JDateChooser();
        ItemPriceLabel2 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        ItemPriceLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePO = new javax.swing.JTable();
        txtPOID = new javax.swing.JTextField();
        txtSearch = new javax.swing.JTextField();
        btnUpdatePO = new javax.swing.JButton();
        ItemNameLabel = new javax.swing.JLabel();
        FlagStatusLabel = new javax.swing.JLabel();
        ItemIDLabel1 = new javax.swing.JLabel();
        txtPRID = new javax.swing.JTextField();
        txtRestockQty = new javax.swing.JTextField();
        FlagStatusLabel1 = new javax.swing.JLabel();
        txtSupplierID = new javax.swing.JTextField();
        FlagStatusLabel2 = new javax.swing.JLabel();
        txtSuppliedPrice = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel14.setText("jLabel14");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("View Purchase Order");

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

        ItemQtyLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemQtyLabel.setText("Item Name:");

        btnCreateNew.setBackground(new java.awt.Color(15, 1, 71));
        btnCreateNew.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCreateNew.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateNew.setText("Generate New PO");
        btnCreateNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateNewActionPerformed(evt);
            }
        });

        btnDeletePO.setBackground(new java.awt.Color(255, 51, 51));
        btnDeletePO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeletePO.setText("Delete");
        btnDeletePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePOActionPerformed(evt);
            }
        });

        ItemIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemIDLabel.setText("PO ID:");

        ItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel1.setText("Search PO:");

        txtItemID.setEnabled(false);

        btnBack1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack1.setText("Back");
        btnBack1.setPreferredSize(new java.awt.Dimension(82, 23));
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
            }
        });

        txtItemName.setEnabled(false);

        btnClearForm.setBackground(new java.awt.Color(255, 153, 51));
        btnClearForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearForm.setText("Cancel");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });

        txtPurchaseDate.setEnabled(false);

        ItemPriceLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemPriceLabel2.setText("Purchase Date:");

        txtStatus.setColumns(5);
        txtStatus.setEnabled(false);
        txtStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStatusActionPerformed(evt);
            }
        });

        ItemPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemPriceLabel.setText("Restock Qty:");

        tablePO.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablePO);

        txtPOID.setEnabled(false);

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnUpdatePO.setBackground(new java.awt.Color(51, 204, 255));
        btnUpdatePO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdatePO.setText("Update");
        btnUpdatePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePOActionPerformed(evt);
            }
        });

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel.setText("Item ID:");

        FlagStatusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FlagStatusLabel.setText("PO Status:");

        ItemIDLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemIDLabel1.setText("PR ID:");

        txtPRID.setEnabled(false);

        txtRestockQty.setEnabled(false);
        txtRestockQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRestockQtyActionPerformed(evt);
            }
        });

        FlagStatusLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FlagStatusLabel1.setText("Supplier ID:");

        txtSupplierID.setColumns(5);
        txtSupplierID.setEnabled(false);
        txtSupplierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierIDActionPerformed(evt);
            }
        });

        FlagStatusLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FlagStatusLabel2.setText("Supplied Price:");

        txtSuppliedPrice.setColumns(5);
        txtSuppliedPrice.setEnabled(false);
        txtSuppliedPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuppliedPriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnUpdatePO, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(btnDeletePO, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ItemNameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPOID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ItemIDLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ItemIDLabel1))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ItemNameLabel)
                                    .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ItemQtyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ItemPriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtRestockQty, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ItemPriceLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPurchaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(FlagStatusLabel))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FlagStatusLabel1)
                                    .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(FlagStatusLabel2)
                                    .addComponent(btnBack1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtSuppliedPrice)
                                        .addGap(7, 7, 7))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(btnCreateNew, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemNameLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreateNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ItemNameLabel)
                                    .addComponent(ItemQtyLabel)
                                    .addComponent(ItemIDLabel)
                                    .addComponent(ItemPriceLabel)
                                    .addComponent(FlagStatusLabel)
                                    .addComponent(ItemPriceLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtStatus)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtRestockQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtPurchaseDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ItemIDLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdatePO, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeletePO, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(FlagStatusLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSuppliedPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(FlagStatusLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void btnCreateNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateNewActionPerformed
       this.dispose();
       new PRApproval(user).setVisible(true);
    }//GEN-LAST:event_btnCreateNewActionPerformed

    private void btnDeletePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePOActionPerformed
        int selectedRow = tablePO.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a PO item to delete.");
            return;
        }

        String poID = tablePO.getValueAt(selectedRow, 0).toString(); // PO ID column
        String itemName = tablePO.getValueAt(selectedRow, 3).toString(); // Item Name column

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete PO \"" + itemName + "\" (PO ID: " + poID + ")?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = FileOperation.deleteWithDependencies(poID, false); // false = PO deletion
            if (success) {
                JOptionPane.showMessageDialog(this, "Purchase Order deleted. Related PR status set to Pending.");

                // Refresh table after deletion
                DefaultTableModel model = manager.getPOTableModel(); // Make sure manager is initialized
                tablePO.setModel(model);

                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                tablePO.setRowSorter(sorter);

                // Optional: set column comparator if necessary
                sorter.setComparator(4, (o1, o2) -> Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString())));

                clearPOForm(); // Clear input fields if any

            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the PO.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Deletion cancelled.");
        }
    }//GEN-LAST:event_btnDeletePOActionPerformed

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        this.dispose();

        if (user instanceof PurchaseManager) {
            new PurchaseManagerMenu((PurchaseManager) user).setVisible(true);
        } else if (user instanceof SalesManager) {
            new SalesManagerMenu((SalesManager) user).setVisible(true);
        } else if (user instanceof Admin) {
            new AdminMenu((Admin) user).setVisible(true);
        } else if (user instanceof FinanceManager) {
            new FinanceManagerMenu((FinanceManager) user).setVisible(true);
        } else if (user instanceof InventoryManager) {
            new InventoryManagerMenu((InventoryManager) user).setVisible(true);
        } else {
            // Optional: handle unknown user type
            JOptionPane.showMessageDialog(null, "Unknown user type. Cannot navigate back.");
        }
    }//GEN-LAST:event_btnBack1ActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
        clearPOForm();
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void btnUpdatePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePOActionPerformed
        String poID = txtPOID.getText();
        String prID = txtPRID.getText();
        String itemID = txtItemID.getText();
        String quantity = txtRestockQty.getText();
        String itemName = txtItemName.getText();
        String status = txtStatus.getText();

        GeneratePO generatePOFrame = new GeneratePO(user, poID, prID, itemID, itemName, quantity, status, true, false);
        generatePOFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnUpdatePOActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusActionPerformed

    private void txtRestockQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRestockQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRestockQtyActionPerformed

    private void txtSupplierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierIDActionPerformed

    private void txtSuppliedPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuppliedPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuppliedPriceActionPerformed

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
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ViewPO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FlagStatusLabel;
    private javax.swing.JLabel FlagStatusLabel1;
    private javax.swing.JLabel FlagStatusLabel2;
    private javax.swing.JLabel ItemIDLabel;
    private javax.swing.JLabel ItemIDLabel1;
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JLabel ItemNameLabel1;
    private javax.swing.JLabel ItemPriceLabel;
    private javax.swing.JLabel ItemPriceLabel2;
    private javax.swing.JLabel ItemQtyLabel;
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnCreateNew;
    private javax.swing.JButton btnDeletePO;
    private javax.swing.JButton btnUpdatePO;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePO;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPOID;
    private javax.swing.JTextField txtPRID;
    private com.toedter.calendar.JDateChooser txtPurchaseDate;
    private javax.swing.JTextField txtRestockQty;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtSuppliedPrice;
    private javax.swing.JTextField txtSupplierID;
    // End of variables declaration//GEN-END:variables
}
