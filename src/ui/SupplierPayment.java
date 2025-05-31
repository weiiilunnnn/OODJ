/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.awt.Color;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.Admin;
import models.FinanceManager;
import models.Payment;
import models.PurchaseOrder;
import models.User;
import services.ItemManager;
import services.POManager;
import services.PaymentManager;
import services.SupplierManager;

/**
 *
 * @author shihongwong
 */
public class SupplierPayment extends javax.swing.JFrame {
    private User user;
    private String PoId;
    private TableRowSorter<DefaultTableModel> sorter;
    private ItemManager itemManager = new ItemManager();
    private PaymentManager paymentManager = new PaymentManager();
    
    public SupplierPayment(User user) {
        initComponents();
        this.user = user;
        setLocationRelativeTo(null);

        loadStockTable(); // load and set model first

        // then get the updated model and create sorter
        DefaultTableModel model = (DefaultTableModel) StockTbl.getModel();
        sorter = new TableRowSorter<>(model);
        StockTbl.setRowSorter(sorter);

        addTableSelectionListener();
        addChangeListenerOnce();
        searchTable();
    }

    private void loadStockTable() {
        String[] columns = {"PO ID", "Item ID", "Supplier ID", "Supplier Item Price", "Quantity Ordered", "Total", "Payment Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        POManager poManager = new POManager();
        PaymentManager paymentManager = new PaymentManager();
        SupplierManager supplierManager = new SupplierManager();

        List<Payment> payments = paymentManager.load(); // load from payment.txt

        for (Payment payment : payments) {
            String poID = payment.getPurchaseOrderID();
            PurchaseOrder po = poManager.getById(poID); // get PO for this payment

            if (po != null) {
                String itemID = po.getItemID().toUpperCase();
                String supplierID = po.getSupplierID();

                double price = supplierManager.getItemPrice(supplierID, itemID);
                int quantity = po.getQuantity();
                double total = price * quantity;
                String status = payment.getStatus().toUpperCase();

                Object[] row = {
                    poID,
                    itemID,
                    supplierID,
                    String.format("RM%.2f", price),
                    quantity,
                    String.format("RM%.2f", total),
                    status
                };

                model.addRow(row);
            }
        }

        StockTbl.setModel(model);
    }



    private void addTableSelectionListener() {
        StockTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = StockTbl.getSelectedRow();
                if (selectedRow != -1) {
                    String poId = StockTbl.getValueAt(selectedRow, 0).toString();
                    String paymentStatus = StockTbl.getValueAt(selectedRow, 6).toString().toUpperCase(); // Assuming column 6 is status

                    PoId = poId; // Store selected PO ID

                    if (paymentStatus.equals("UNPAID")) {
                        // Enable Proceed Payment
                        ProceedPaymentBtn.setEnabled(true);
                        ProceedPaymentBtn.setBackground(new java.awt.Color(102, 204, 255)); // Blue
                        ViewRecieptBtn.setEnabled(false);
                        ViewRecieptBtn.setBackground(Color.GRAY);
                    } else if (paymentStatus.equals("PAID")) {
                        // Enable View Receipt
                        ViewRecieptBtn.setEnabled(true);
                        ViewRecieptBtn.setBackground(new java.awt.Color(102, 204, 255)); // Blue
                        ProceedPaymentBtn.setEnabled(false);
                        ProceedPaymentBtn.setBackground(Color.GRAY);
                    } else {
                        // Default fallback
                        ProceedPaymentBtn.setEnabled(false);
                        ProceedPaymentBtn.setBackground(Color.GRAY);
                        ViewRecieptBtn.setEnabled(false);
                        ViewRecieptBtn.setBackground(Color.GRAY);
                    }
                }
            }
        });
    }
    
    private void searchTable() {
        String searchText = SearchTf.getText().trim();

        if (searchText.length() == 0) {
            sorter.setRowFilter(null); // show all if search is empty
        } else {
            // (?i) makes it case-insensitive
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
        }
    }
    
    private void addChangeListenerOnce() {
        SearchTf.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchTable();
            }
        });
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanel1 = new javax.swing.JPanel();
        ItemNameLabel1 = new javax.swing.JLabel();
        SearchTf = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ProceedPaymentBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        StockTbl = new javax.swing.JTable();
        ViewRecieptBtn = new javax.swing.JButton();
        OptionBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel1.setBackground(new java.awt.Color(255, 255, 255));

        ItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemNameLabel1.setText("Search PO:");

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/OSWBsmaller.png"))); // NOI18N
        jLabel14.setText("jLabel14");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Supplier Payment");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(240, Short.MAX_VALUE))
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

        ProceedPaymentBtn.setText("Proceed Payment");
        ProceedPaymentBtn.setEnabled(false);
        ProceedPaymentBtn.setBackground(Color.GRAY);
        ProceedPaymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProceedPaymentBtnActionPerformed(evt);
            }
        });

        StockTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Purchase Order ID", "Item ID", "Supplier ID", "Price", "Quantity Order", "Total", "Payment Status"
            }
        ));
        jScrollPane2.setViewportView(StockTbl);

        ViewRecieptBtn.setText("View Reciept");
        ViewRecieptBtn.setEnabled(false);
        ViewRecieptBtn.setBackground(Color.GRAY);
        ViewRecieptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewRecieptBtnActionPerformed(evt);
            }
        });

        OptionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Paid", "Unpaid" }));
        OptionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptionBoxActionPerformed(evt);
            }
        });

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPanel1Layout = new javax.swing.GroupLayout(JPanel1);
        JPanel1.setLayout(JPanel1Layout);
        JPanel1Layout.setHorizontalGroup(
            JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(JPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ViewRecieptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ProceedPaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPanel1Layout.createSequentialGroup()
                        .addComponent(ItemNameLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SearchTf, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(430, 430, 430)
                        .addComponent(OptionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        JPanel1Layout.setVerticalGroup(
            JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ItemNameLabel1)
                        .addComponent(SearchTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(OptionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ProceedPaymentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ViewRecieptBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProceedPaymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedPaymentBtnActionPerformed
        System.out.println("po id: " + PoId);
        ProcessSupplierPayment psp = new ProcessSupplierPayment(user, PoId);
        this.dispose();
        psp.setVisible(true);
    }//GEN-LAST:event_ProceedPaymentBtnActionPerformed

    private void OptionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OptionBoxActionPerformed
        String selectedOption = OptionBox.getSelectedItem().toString();

        if (selectedOption.equalsIgnoreCase("All")) {
            sorter.setRowFilter(null); // show all
        } else if (selectedOption.equalsIgnoreCase("Paid")) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)^PAID$", 6));
        } else if (selectedOption.equalsIgnoreCase("Unpaid")) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)^UNPAID$", 6));
        }
    }//GEN-LAST:event_OptionBoxActionPerformed

    private void ViewRecieptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewRecieptBtnActionPerformed
        GenerateReceipt gr = new GenerateReceipt(user, PoId);
        this.dispose();
        gr.setVisible(true);
    }//GEN-LAST:event_ViewRecieptBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();

        if (user instanceof Admin) {
            new AdminMenu((Admin) user).setVisible(true);
        } else if (user instanceof FinanceManager) {
            new FinanceManagerMenu((FinanceManager) user).setVisible(true);
        } else {
            // Optional: handle unknown user type
            JOptionPane.showMessageDialog(null, "Unknown user type. Cannot navigate back.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(SupplierPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SupplierPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SupplierPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SupplierPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ItemNameLabel1;
    private javax.swing.JPanel JPanel1;
    private javax.swing.JComboBox<String> OptionBox;
    private javax.swing.JButton ProceedPaymentBtn;
    private javax.swing.JTextField SearchTf;
    private javax.swing.JTable StockTbl;
    private javax.swing.JButton ViewRecieptBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
