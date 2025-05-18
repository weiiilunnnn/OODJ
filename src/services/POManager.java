/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.PurchaseOrder;

/**
 *
 * @author lunwe
 */
public class POManager extends MainManager<PurchaseOrder>{
    public POManager() {
        super("PurchaseOrder.txt");
    }
    
    @Override
    protected PurchaseOrder parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 8) {
            return new PurchaseOrder(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), parts[5], parts[6], parts[7]);
        }
        return null;
    }
    
    @Override
    protected String toLine(PurchaseOrder po) {
        return po.toFileFormat();
    }

    @Override
    public void delete(String id, List<PurchaseOrder> list) {
        list.removeIf(p -> p.getPoID().equals(id));
        save(list);
    }

    @Override
    public void update(PurchaseOrder po, List<PurchaseOrder> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPrID().equals(po.getPoID())) {
                list.set(i, po);
                break;
            }
        }
        save(list);
    }
    
    public DefaultTableModel getPRTableModel() {
        List<PurchaseOrder> poList = load();
        String[] columns = {"PO ID", "PR ID", "Item ID", "Item Name", "Restock Qty", "PO Raised By", "Required Date", "Status"};

        return MainManager.getTableModel(poList, columns, po -> new Object[]{
            po.getPoID(), po.getPrID(), po.getItemID(), po.getItemName(), po.getQuantity(),
            po.getRaisedBy(), po.getDate(), po.getStatus()
        });
    }
    
}
