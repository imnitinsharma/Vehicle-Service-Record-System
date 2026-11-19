package com.vehicle.ui;

import com.vehicle.dao.ServiceRecordDAO;
import com.vehicle.dao.VehicleDAO;
import com.vehicle.model.ServiceRecord;
import com.vehicle.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ServicePanel extends JPanel {

    private JComboBox<String> vehicleCombo;
    private JTextField dateField, typeField, mechanicField, costField, nextDateField;
    private JTextArea descArea;
    private JComboBox<String> statusCombo;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn, refreshBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedServiceId = -1;

    private ServiceRecordDAO serviceDAO = new ServiceRecordDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private List<Vehicle> vehicles;

    public ServicePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initComponents();
        loadTable();
    }

    private void initComponents() {
        // ── Form Panel ───────────────────────────────────────────
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(new Color(245, 248, 255));
        formCard.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230)), "Service Record Details",
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(30, 60, 114)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 8, 4, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        vehicleCombo = new JComboBox<>();
        vehicleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadVehicles();

        dateField     = new JTextField("2024-01-01", 14);
        typeField     = new JTextField(18);
        mechanicField = new JTextField(18);
        costField     = new JTextField("0.00", 10);
        nextDateField = new JTextField("2024-07-01", 14);
        descArea      = new JTextArea(2, 20);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        statusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Row 0: Vehicle  |  Row 1: Date  |  Row 2: Type  |  Row 3: Mechanic  |  Row 4: Cost  |  Row 5: Status  |  Row 6: Next  |  Row 7: Desc
        Object[][] rows = {
            {"Vehicle *", vehicleCombo},
            {"Service Date * (yyyy-MM-dd)", dateField},
            {"Service Type *", typeField},
            {"Mechanic Name", mechanicField},
            {"Cost (₹)", costField},
            {"Status", statusCombo},
            {"Next Service Date", nextDateField},
            {"Description", new JScrollPane(descArea)}
        };

        for (int i = 0; i < rows.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            JLabel l = new JLabel(rows[i][0] + ":"); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            formCard.add(l, gc);
            gc.gridx = 1; gc.weightx = 1;
            if (rows[i][1] instanceof JTextField) ((JTextField) rows[i][1]).setFont(new Font("Segoe UI", Font.PLAIN, 12));
            formCard.add((Component) rows[i][1], gc);
        }

        addBtn     = CustomerPanel.makeBtn("➕ Add",     new Color(34, 139, 34));
        updateBtn  = CustomerPanel.makeBtn("✏ Update",  new Color(70, 130, 180));
        deleteBtn  = CustomerPanel.makeBtn("🗑 Delete",  new Color(180, 50, 50));
        clearBtn   = CustomerPanel.makeBtn("✖ Clear",   new Color(130, 130, 130));
        refreshBtn = CustomerPanel.makeBtn("🔄 Refresh", new Color(60, 100, 160));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        btnRow.setBackground(new Color(245, 248, 255));
        btnRow.add(addBtn); btnRow.add(updateBtn); btnRow.add(deleteBtn);
        btnRow.add(clearBtn); btnRow.add(refreshBtn);

        gc.gridx = 0; gc.gridy = rows.length; gc.gridwidth = 2;
        formCard.add(btnRow, gc);

        // ── Table ────────────────────────────────────────────────
        String[] cols = {"ID", "Vehicle No.", "Date", "Type", "Mechanic", "Cost (₹)", "Status", "Next Service"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        CustomerPanel.styleTable(table);
        int[] widths = {40, 110, 95, 130, 110, 80, 100, 100};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Status renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String status = v != null ? v.toString() : "";
                if (status.equals("Completed")) setForeground(new Color(0, 128, 0));
                else if (status.equals("In Progress")) setForeground(new Color(200, 100, 0));
                else setForeground(new Color(180, 0, 0));
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        add(formCard, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        // ── Listeners ────────────────────────────────────────────
        addBtn.addActionListener(e -> addRecord());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> { loadTable(); loadVehicles(); });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedServiceId = (int) tableModel.getValueAt(row, 0);
                dateField.setText(tableModel.getValueAt(row, 2).toString());
                typeField.setText(tableModel.getValueAt(row, 3).toString());
                mechanicField.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
                costField.setText(tableModel.getValueAt(row, 5).toString());
                statusCombo.setSelectedItem(tableModel.getValueAt(row, 6));
                Object nd = tableModel.getValueAt(row, 7);
                nextDateField.setText(nd != null && !nd.equals("—") ? nd.toString() : "");
            }
        });
    }

    private void loadVehicles() {
        vehicles = vehicleDAO.getAllVehicles();
        vehicleCombo.removeAllItems();
        vehicleCombo.addItem("-- Select Vehicle --");
        for (Vehicle v : vehicles) vehicleCombo.addItem(v.getVehicleNumber() + " — " + v.getMake() + " " + v.getModel());
    }

    private void addRecord() {
        if (!validateForm()) return;
        ServiceRecord sr = buildRecord(0);
        if (serviceDAO.addServiceRecord(sr)) {
            JOptionPane.showMessageDialog(this, "✅ Service record added!");
            clearForm(); loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        if (selectedServiceId == -1) { JOptionPane.showMessageDialog(this, "Select a record to update."); return; }
        if (!validateForm()) return;
        ServiceRecord sr = buildRecord(selectedServiceId);
        if (serviceDAO.updateServiceRecord(sr)) {
            JOptionPane.showMessageDialog(this, "✅ Service record updated!");
            clearForm(); loadTable();
        }
    }

    private void deleteRecord() {
        if (selectedServiceId == -1) { JOptionPane.showMessageDialog(this, "Select a record to delete."); return; }
        int c = JOptionPane.showConfirmDialog(this, "Delete this service record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            if (serviceDAO.deleteServiceRecord(selectedServiceId)) {
                JOptionPane.showMessageDialog(this, "✅ Record deleted!");
                clearForm(); loadTable();
            }
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (ServiceRecord sr : serviceDAO.getAllServiceRecords()) {
            tableModel.addRow(new Object[]{
                sr.getServiceId(), sr.getVehicleNumber(), sr.getServiceDate(),
                sr.getServiceType(), sr.getMechanicName(),
                String.format("%.2f", sr.getCost()), sr.getStatus(),
                sr.getNextServiceDate() != null ? sr.getNextServiceDate() : "—"
            });
        }
    }

    private boolean validateForm() {
        if (vehicleCombo.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Select a vehicle."); return false; }
        if (dateField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Service date required (yyyy-MM-dd)."); return false; }
        if (typeField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Service type required."); return false; }
        return true;
    }

    private ServiceRecord buildRecord(int id) {
        ServiceRecord sr = new ServiceRecord();
        sr.setServiceId(id);
        sr.setVehicleId(vehicles.get(vehicleCombo.getSelectedIndex() - 1).getVehicleId());
        try { sr.setServiceDate(Date.valueOf(dateField.getText().trim())); } catch (Exception e) { sr.setServiceDate(Date.valueOf("2024-01-01")); }
        sr.setServiceType(typeField.getText().trim());
        sr.setDescription(descArea.getText().trim());
        sr.setMechanicName(mechanicField.getText().trim());
        try { sr.setCost(Double.parseDouble(costField.getText().trim())); } catch (Exception e) { sr.setCost(0); }
        sr.setStatus(statusCombo.getSelectedItem().toString());
        try { sr.setNextServiceDate(Date.valueOf(nextDateField.getText().trim())); } catch (Exception e) { sr.setNextServiceDate(null); }
        return sr;
    }

    private void clearForm() {
        vehicleCombo.setSelectedIndex(0); dateField.setText("2024-01-01"); typeField.setText("");
        mechanicField.setText(""); costField.setText("0.00"); statusCombo.setSelectedIndex(0);
        nextDateField.setText(""); descArea.setText("");
        selectedServiceId = -1; table.clearSelection();
    }
}
