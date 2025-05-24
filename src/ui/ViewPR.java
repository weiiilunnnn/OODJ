/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.PurchaseRequisition;
import models.SalesManager;
import models.User;
import services.FileOperation;
import services.PRManager;

/**
 *
 * @author lunwe
 */
public class ViewPR extends javax.swing.JFrame {
    private User user;
    private PRManager manager = new PRManager();
    
    private List<PurchaseRequisition> PRList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private Map<String, String> itemMap;
    
    /**
     * Creates new form ViewPR
     */
    public ViewPR(User user) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        PRList = manager.load();
        tableModel = manager.getPRTableModel();
        tablePR.setModel(tableModel);
        itemMap = FileOperation.getItemMap();
        
        tablePR.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int row = tablePR.getSelectedRow();
            if (row >= 0 && tablePR.getRowCount() > 0 && tablePR.getColumnCount() >= 5) {
                try {
                    txtPRID.setText(tablePR.getValueAt(row, 0).toString());

                    String itemId = tablePR.getValueAt(row, 1).toString();
                    txtItemID.setText(itemId);

                    String itemName = itemMap.getOrDefault(itemId, "Unknown Item");
                    txtItemName.setText(itemName);

                    txtRestockQty.setText(tablePR.getValueAt(row, 3).toString());

                    String dateStr = tablePR.getValueAt(row, 5).toString(); // "Required Date"
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formatter.parse(dateStr);
                        txtRequiredDate.setDate(date);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to parse date: " + ex.getMessage());
                    }

                    txtPRStatus.setText(tablePR.getValueAt(row, 6).toString());
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
        // Validate Restock Quantity
        try {
            int restockQty = Integer.parseInt(txtRestockQty.getText().trim());
            if (restockQty <= 0) {
                JOptionPane.showMessageDialog(this, "Restock quantity must be more than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Restock quantity must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Required Date
        if (txtRequiredDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a required date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Optional: Prevent selecting a past date
        Date today = new Date();
        if (txtRequiredDate.getDate().before(today)) {
            JOptionPane.showMessageDialog(this, "Required date cannot be in the past.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void clearPRForm() {
        txtPRID.setText("");
        txtItemName.setText("");
        txtRestockQty.setText("");
        txtItemID.setText("");
        txtPRStatus.setText("");
        txtRequiredDate.setDate(null); // This clears the JDateChooser
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtPRID = new javax.swing.JTextField();
        txtSearch = new javax.swing.JTextField();
        btnUpdateItem = new javax.swing.JButton();
        ItemNameLabel = new javax.swing.JLabel();
        ItemQtyLabel = new javax.swing.JLabel();
        btnDeleteItem = new javax.swing.JButton();
        ItemNameLabel1 = new javax.swing.JLabel();
        btnBack1 = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();
        txtRestockQty = new javax.swing.JTextField();
        txtPRStatus = new javax.swing.JTextField();
        ItemPriceLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePR = new javax.swing.JTable();
        FlagStatusLabel = new javax.swing.JLabel();
        btnCreateNew = new javax.swing.JButton();
        ItemIDLabel = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtRequiredDate = new com.toedter.calendar.JDateChooser();
        ItemPriceLabel2 = new javax.swing.JLabel();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("View Purchase Requisition");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel14.setText("jLabel14");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("View Purchase Requisition");

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

        txtPRID.setEnabled(false);

        btnUpdateItem.setBackground(new java.awt.Color(51, 204, 255));
        btnUpdateItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateItem.setText("Update");
        btnUpdateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateItemActionPerformed(evt);
            }
        });

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel.setText("Item ID:");

        ItemQtyLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemQtyLabel.setText("Item Name:");

        btnDeleteItem.setBackground(new java.awt.Color(255, 51, 51));
        btnDeleteItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteItem.setText("Delete");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        ItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel1.setText("Search PR:");

        btnBack1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack1.setText("Back");
        btnBack1.setPreferredSize(new java.awt.Dimension(82, 23));
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
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

        txtPRStatus.setColumns(5);
        txtPRStatus.setEnabled(false);

        ItemPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemPriceLabel.setText("Restock Qty:");

        tablePR.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablePR);

        FlagStatusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        FlagStatusLabel.setText("PR Status:");

        btnCreateNew.setBackground(new java.awt.Color(15, 1, 71));
        btnCreateNew.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCreateNew.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateNew.setText("+ Create New");
        btnCreateNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateNewActionPerformed(evt);
            }
        });

        ItemIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemIDLabel.setText("PR ID:");

        txtItemID.setEnabled(false);

        txtItemName.setEnabled(false);

        ItemPriceLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemPriceLabel2.setText("Required Date:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(ItemNameLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnUpdateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(364, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCreateNew, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ItemIDLabel))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ItemNameLabel)
                                        .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ItemQtyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtRestockQty, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ItemPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(19, 19, 19)
                                            .addComponent(ItemPriceLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(txtRequiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPRStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(FlagStatusLabel)))
                                .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemNameLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreateNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemNameLabel)
                    .addComponent(ItemQtyLabel)
                    .addComponent(ItemIDLabel)
                    .addComponent(ItemPriceLabel)
                    .addComponent(FlagStatusLabel)
                    .addComponent(ItemPriceLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPRStatus)
                    .addComponent(txtRequiredDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtRestockQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
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

    private void btnUpdateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateItemActionPerformed
        int selectedRow = tablePR.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
            return;
        }

        if (!validateInputs()) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to update this restock info?",
            "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String prID = txtPRID.getText().trim();
            String itemID = txtItemID.getText().trim();
            String itemName = txtItemName.getText().trim();
            int restockQty = Integer.parseInt(txtRestockQty.getText().trim());
            String raisedBy = tablePR.getValueAt(selectedRow, 4).toString();
            Date requiredDate = txtRequiredDate.getDate();
            String status = txtPRStatus.getText().trim();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String requiredDateStr = sdf.format(requiredDate);

            // Create updated PR object
            PurchaseRequisition updatedPR = new PurchaseRequisition(
                prID, itemID, restockQty, raisedBy, requiredDateStr, status
            );

            // Load all PRs, update the matching one, then save
            List<PurchaseRequisition> prList = manager.load();
            manager.update(updatedPR, prList);  // <- this saves the file!

            // Refresh table
            tablePR.setModel(manager.getPRTableModel());

            JOptionPane.showMessageDialog(this, "Restock info updated successfully.");
            btnClearForm.doClick();
        } else {
            JOptionPane.showMessageDialog(this, "Update cancelled.");
        }
    }//GEN-LAST:event_btnUpdateItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        int selectedRow = tablePR.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a PR item to delete.");
            return;
        }

        String prID = tablePR.getValueAt(selectedRow, 0).toString(); // PR ID column
        String itemName = tablePR.getValueAt(selectedRow, 2).toString(); // Item Name column

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete PR \"" + itemName + "\" (PR ID: " + prID + ")?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = FileOperation.deleteWithDependencies(prID, true); // true = PR deletion
            if (success) {
                JOptionPane.showMessageDialog(this, "Purchase Requisition and related POs deleted.");

                // Refresh table after deletion
                DefaultTableModel model = manager.getPRTableModel(); // Make sure manager is initialized
                tablePR.setModel(model);

                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                tablePR.setRowSorter(sorter);

                // Optional: set column comparator if necessary
                sorter.setComparator(3, (o1, o2) -> Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString())));

                clearPRForm(); // Clear input fields if any

            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the PR.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Deletion cancelled.");
        }
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        this.dispose();
        new SalesManagerMenu(user).setVisible(true);
    }//GEN-LAST:event_btnBack1ActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
       clearPRForm();
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void btnCreateNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateNewActionPerformed
        this.dispose();
        new CreatePR((SalesManager) user).setVisible(true);
    }//GEN-LAST:event_btnCreateNewActionPerformed

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
            java.util.logging.Logger.getLogger(ViewPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ViewPR().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FlagStatusLabel;
    private javax.swing.JLabel ItemIDLabel;
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JLabel ItemNameLabel1;
    private javax.swing.JLabel ItemPriceLabel;
    private javax.swing.JLabel ItemPriceLabel2;
    private javax.swing.JLabel ItemQtyLabel;
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnCreateNew;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnUpdateItem;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTable tablePR;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPRID;
    private javax.swing.JTextField txtPRStatus;
    private com.toedter.calendar.JDateChooser txtRequiredDate;
    private javax.swing.JTextField txtRestockQty;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
