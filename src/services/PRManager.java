/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.PurchaseRequisition;

/**
 *
 * @author lunwe
 */
public class PRManager extends MainManager<PurchaseRequisition>{
    public PRManager() {
        super("PurchaseRequisition.txt");
    }

    @Override
    protected PurchaseRequisition parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 7) {
            return new PurchaseRequisition(parts[0], parts[1], parts[2],
                Integer.parseInt(parts[3]), parts[4], parts[5], parts[6]);
        }
        return null;
    }

    @Override
    protected String toLine(PurchaseRequisition pr) {
        return pr.toFileFormat();
    }

    @Override
    public void delete(String id, List<PurchaseRequisition> list) {
        list.removeIf(p -> p.getPrID().equals(id));
        save(list);
    }

    @Override
    public void update(PurchaseRequisition pr, List<PurchaseRequisition> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPrID().equals(pr.getPrID())) {
                list.set(i, pr);
                break;
            }
        }
        save(list);
    }
    
    public DefaultTableModel getPRTableModel() {
        List<PurchaseRequisition> prList = load();
        String[] columns = {"PR ID", "Item ID", "Item Name", "Restock Qty", "PR Raised By", "Required Date", "Status"};

        return MainManager.getTableModel(prList, columns, pr -> new Object[]{
            pr.getPrID(), pr.getItemID(), pr.getItemName(), pr.getQuantity(),
            pr.getRaisedBy(), pr.getDate(), pr.getStatus()
        });
    }
    
}
