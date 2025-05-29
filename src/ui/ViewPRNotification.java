/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.PurchaseRequisition;
import models.User;
import services.FileOperation;
import services.MainManager;
import services.NotificationManager;
import services.PRManager;

public class ViewPRNotification extends javax.swing.JDialog {
    
    private static User user;
    private static JFrame purchaseManagerFrame;
    private PRManager manager = new PRManager();
    private NotificationManager nmanager = new NotificationManager();
    
    private List<PurchaseRequisition> PRList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private Map<String, String> itemMap;
    
    public ViewPRNotification(java.awt.Frame parent, boolean modal, User user, JFrame purchaseManagerFrame) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.user = user;
        this.purchaseManagerFrame = purchaseManagerFrame;

        PRList = manager.load();
        itemMap = FileOperation.getItemMap();
        String[] selectedColumns = { "PR ID", "Item ID", "PR Raised By", "Status" };

        List<PurchaseRequisition> pendingPRs = PRList.stream()
            .filter(pr -> pr.getStatus().equalsIgnoreCase("Pending"))
            .collect(java.util.stream.Collectors.toList());

        // Build filtered table model
        DefaultTableModel filteredModel = MainManager.getTableModel(
            pendingPRs,
            selectedColumns,
            pr -> new Object[] {
                pr.getPrID(),
                pr.getItemID(),
                pr.getRaisedBy(),
                pr.getStatus()
            }
        );

        tableModel = nmanager.getLast10RowsOnly(filteredModel);
        tblNotifications.setModel(tableModel);
        tblNotifications.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tblNotifications.getSelectedRow();
                    if (row != -1) {
                        handleRowClick(row);
                    }
                }
            }
        });
    }
    
private void handleRowClick(int row) {
    int modelRow = tblNotifications.convertRowIndexToModel(row);
    String status = tableModel.getValueAt(modelRow, 3).toString().trim(); 

    if (status.equalsIgnoreCase("Pending")) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Do you want to proceed with approving this Purchase Requisition?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new PRApproval(user).setVisible(true);
            purchaseManagerFrame.dispose();
            this.dispose(); 
        }

    } else if (status.equalsIgnoreCase("Approved")) {
        JOptionPane.showMessageDialog(this, "The Purchase Requisition has been Approved.");
    } else if (status.equalsIgnoreCase("Rejected")) {
        JOptionPane.showMessageDialog(this, "The Purchase Requisition has been Rejected.");                            
    } else {
        JOptionPane.showMessageDialog(this, "Unknown status: " + status);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        tblPRNotifications = new javax.swing.JScrollPane();
        tblNotifications = new javax.swing.JTable();
        btnBack1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Notifications");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        tblNotifications.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNotifications.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNotificationsMouseClicked(evt);
            }
        });
        tblPRNotifications.setViewportView(tblNotifications);

        btnBack1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack1.setText("Back");
        btnBack1.setPreferredSize(new java.awt.Dimension(82, 23));
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tblPRNotifications, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tblPRNotifications, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBack1ActionPerformed

    private void tblNotificationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNotificationsMouseClicked

    }//GEN-LAST:event_tblNotificationsMouseClicked

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
            java.util.logging.Logger.getLogger(ViewPRNotification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPRNotification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPRNotification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPRNotification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ViewPRNotification dialog = new ViewPRNotification(new javax.swing.JFrame(), true, user, purchaseManagerFrame);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable tblNotifications;
    private javax.swing.JScrollPane tblPRNotifications;
    // End of variables declaration//GEN-END:variables
}
