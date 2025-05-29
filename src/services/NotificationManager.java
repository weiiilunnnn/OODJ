package services;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class NotificationManager {
        
    public DefaultTableModel getLast10RowsOnly(DefaultTableModel fullModel) {
        int rowCount = fullModel.getRowCount();
        int colCount = fullModel.getColumnCount();
        int start = Math.max(0, rowCount - 10);

        // Create a new model with same column names
        DefaultTableModel limitedModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        for (int col = 0; col < colCount; col++) {
            limitedModel.addColumn(fullModel.getColumnName(col));
        }

        // Copy last 10 rows
        for (int i = start; i < rowCount; i++) {
            Object[] rowData = new Object[colCount];
            for (int j = 0; j < colCount; j++) {
                rowData[j] = fullModel.getValueAt(i, j);
            }
            limitedModel.addRow(rowData);
        }

        return limitedModel;
    }
    
    public DefaultTableModel filterColumns(DefaultTableModel fullModel, String[] selectedColumns) {
        DefaultTableModel filteredModel = new DefaultTableModel(selectedColumns, 0);

        // Get the indices of selected columns in the full model
        List<Integer> colIndices = new ArrayList<>();
        for (String colName : selectedColumns) {
            int index = fullModel.findColumn(colName);
            if (index != -1) {
                colIndices.add(index);
            }
        }

        for (int i = 0; i < fullModel.getRowCount(); i++) {
            Object[] rowData = new Object[colIndices.size()];
            for (int j = 0; j < colIndices.size(); j++) {
                rowData[j] = fullModel.getValueAt(i, colIndices.get(j));
            }
            filteredModel.addRow(rowData);
        }

        return filteredModel;
    }

    

    
}
