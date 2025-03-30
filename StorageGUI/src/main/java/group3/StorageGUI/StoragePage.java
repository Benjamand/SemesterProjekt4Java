package group3.StorageGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StoragePage {
    private JFrame frame;
    private JTable warehouseTable;
    private JTable assemblerTable;
    private Map<String, Object[][]> tableData; // Data holder for tables

    public StoragePage() {
        tableData = new HashMap<>();
        initializeUI();
        fetchAndUpdateData();
    }

    private void initializeUI() {
        frame = new JFrame("Storage Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(2, 1));

        // Initialize tables with empty models
        warehouseTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Content"}, 0));
        assemblerTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Content"}, 0));

        frame.add(createTablePanel("Warehouse Table", warehouseTable));
        frame.add(createTablePanel("Assembler Table", assemblerTable));

        frame.setVisible(true);
    }

    private JPanel createTablePanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void fetchAndUpdateData() {
        // Test for input ad data
        Object[][] warehouseData = { {1, "Item A"}, {2, "Item B"} };
        Object[][] assemblerData = { {3, "Item C"}, {4, "Item D"} };

        tableData.put("Warehouse Table", warehouseData);
        tableData.put("Assembler Table", assemblerData);

        updateTable(warehouseTable, warehouseData);
        updateTable(assemblerTable, assemblerData);
    }

    private void updateTable(JTable table, Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        // new StoragePage();
        SwingUtilities.invokeLater(StoragePage::new);
    }
}
