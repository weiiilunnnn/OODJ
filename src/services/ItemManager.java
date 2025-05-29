package services;

import java.util.*;
import javax.swing.table.DefaultTableModel;
import models.Item;

/**
 * This class handles the business logic for managing inventory items.
 * Demonstrates separation of concerns, file handling, list manipulation, and abstraction in OODJ.
 */

public class ItemManager extends MainManager<Item>{
    private String itemID;
    
    public ItemManager() {
        super("Item.txt");
    }
    
    @Override
    protected Item parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            return new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
        }
        return null;
    }
    
    @Override
    protected String toLine(Item item) {
        return item.toDataString();
    }
    
    @Override
    public void delete(String id, List<Item> list) {
        list.removeIf(p -> p.getItemID().equals(id));
        save(list);
    }
    
    @Override
    public void update(Item item, List<Item> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemID().equals(item.getItemID())) {
                list.set(i, item);
                break;
            }
        }
        save(list);
    } 
    
    
    public DefaultTableModel getItemTableModel() {
        List<Item> itemList = load();
        String[] columns = {"Item ID", "Name", "Qty", "Sales Price", "ROL", "Flag"};

        return MainManager.getTableModel(itemList, columns, item -> new Object[]{
            item.getItemID(), item.getItemName(), item.getItemQty(),
            item.getItemPrice(), item.getReorderLevel(), item.getFlagStatus()
        });
    }
    
    public void updateQuantity(String itemId, int newQty) {
        List<Item> items = load();
        boolean updated = false;

        for (Item item : items) {
            if (item.getItemID().equals(itemId)) {
                item.setItemQty(newQty);
                updated = true;
                break;
            }
        }

        if (updated) {
            save(items);
            System.out.println("Item quantity updated successfully.");
        } else {
            System.err.println("Item with ID " + itemId + " not found.");
        }
    }

    public Item findItemById(String id) {
        List<Item> items = load();
        for (Item item : items) {
            if (item.getItemID().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }
}
