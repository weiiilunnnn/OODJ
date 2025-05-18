/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.ItemDependentRecord;

/**
 *
 * @author lunwe
 */
public abstract class MainManager<T> {
    protected String filePath;

    public MainManager(String filePath) {
        this.filePath = filePath;
    }

    // Must be implemented in subclass
    protected abstract T parseLine(String line);
    protected abstract String toLine(T obj);

    public List<T> load() {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T obj = parseLine(line);
                if (obj != null) {
                    list.add(obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void save(List<T> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T obj : list) {
                writer.write(toLine(obj));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T obj, List<T> list) {
        list.add(obj);
        save(list);
    }

    public abstract void delete(String id, List<T> list);
    public abstract void update(T updated, List<T> list);
    
    @FunctionalInterface
    public interface TableRowMapper<T> {
        Object[] mapRow(T obj);
    }
    
    public static <T> DefaultTableModel getTableModel(List<T> list, String[] columns, TableRowMapper<T> rowMapper) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (T item : list) {
            model.addRow(rowMapper.mapRow(item));
        }
        return model;
    }
    
    public <T extends ItemDependentRecord> void deleteItemReferencesFromFile(
        String itemId, MainManager<T> specificManager) {

        List<T> list = specificManager.load(); // Load from file inside
        list.removeIf(record -> record.getItemID().equals(itemId));
        specificManager.save(list); // Save back to file
    }
    
}
