package com.vehicle.ui;

import com.vehicle.dao.CustomerDAO;
import com.vehicle.dao.VehicleDAO;
import com.vehicle.model.Customer;
import com.vehicle.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehiclePanel extends JPanel {

    private JComboBox<String> customerCombo;
    private JTextField vNumberField, makeField, modelField, yearField, colorField;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedId = -1;

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private List<Customer> customers;

    public VehiclePanel() {
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
            BorderFactory.createLineBorder(new Color(180, 200, 230)), "Vehicle Details",
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(30, 60, 114)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 8, 5, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        customerCombo = new JComboBox<>();
        customerCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadCustomers();

        vNumberField = new JTextField(18); makeField  = new JTextField(18);
        modelField   = new JTextField(18); yearField  = new JTextField(18);
        colorField   = new JTextField(18);

        String[] labels = {"Customer *", "Vehicle Number *", "Make *", "Model *", "Year", "Color"};
        Component[] inputs = {customerCombo, vNumberField, makeField, modelField, yearField, colorField};

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            JLabel l = new JLabel(labels[i] + ":"); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            formCard.add(l, gc);
            gc.gridx = 1; gc.weightx = 1;
            if (inputs[i] instanceof JTextField) ((JTextField)inputs[i]).setFont(new Font("Segoe UI", Font.PLAIN, 12));
            formCard.add(inputs[i], gc);
        }

        addBtn    = CustomerPanel.makeBtn("➕ Add",    new Color(34, 139, 34));
        updateBtn = CustomerPanel.makeBtn("✏ Update", new Color(70, 130, 180));
        deleteBtn = CustomerPanel.makeBtn("🗑 Delete", new Color(180, 50, 50));
        clearBtn  = CustomerPanel.makeBtn("✖ Clear",  new Color(130, 130, 130));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnRow.setBackground(new Color(245, 248, 255));
        btnRow.add(addBtn); btnRow.add(updateBtn); btnRow.add(deleteBtn); btnRow.add(clearBtn);

        gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 2;
        formCard.add(btnRow, gc);

        // ── Table ────────────────────────────────────────────────
        String[] cols = {"ID", "Vehicle Number", "Make", "Model", "Year", "Color", "Owner"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        CustomerPanel.styleTable(table);
        int[] widths = {40, 130, 100, 100, 60, 80, 160};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        add(formCard, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        // ── Listeners ────────────────────────────────────────────
        addBtn.addActionListener(e -> addVehicle());
        updateBtn.addActionListener(e -> updateVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        clearBtn.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) tableModel.getValueAt(row, 0);
                vNumberField.setText(tableModel.getValueAt(row, 1).toString());
                makeField.setText(tableModel.getValueAt(row, 2).toString());
                modelField.setText(tableModel.getValueAt(row, 3).toString());
                yearField.setText(tableModel.getValueAt(row, 4).toString());
                colorField.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
            }
        });
    }

    private void loadCustomers() {
        customers = customerDAO.getAllCustomers();
        customerCombo.removeAllItems();
        customerCombo.addItem("-- Select Customer --");
        for (Customer c : customers) customerCombo.addItem(c.toString());
    }

    private void addVehicle() {
        if (!validateForm()) return;
        Vehicle v = buildVehicle(0);
        if (dao().addVehicle(v)) {
            JOptionPane.showMessageDialog(this, "✅ Vehicle added successfully!");
            clearForm(); loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed. Vehicle number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Select a vehicle."); return; }
        if (!validateForm()) return;
        Vehicle v = buildVehicle(selectedId);
        if (dao().updateVehicle(v)) {
            JOptionPane.showMessageDialog(this, "✅ Vehicle updated!");
            clearForm(); loadTable();
        }
    }

    private void deleteVehicle() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Select a vehicle."); return; }
        int c = JOptionPane.showConfirmDialog(this, "Delete vehicle and all its service records?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            if (dao().deleteVehicle(selectedId)) {
                JOptionPane.showMessageDialog(this, "✅ Vehicle deleted!");
                clearForm(); loadTable();
            }
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Vehicle v : vehicleDAO.getAllVehicles()) {
            tableModel.addRow(new Object[]{
                v.getVehicleId(), v.getVehicleNumber(), v.getMake(), v.getModel(),
                v.getYear(), v.getColor(), v.getCustomerName()
            });
        }
    }

    private boolean validateForm() {
        if (customerCombo.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Select a customer."); return false; }
        if (vNumberField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Vehicle Number required."); return false; }
        if (makeField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Make required."); return false; }
        if (modelField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Model required."); return false; }
        return true;
    }

    private Vehicle buildVehicle(int id) {
        Vehicle v = new Vehicle();
        v.setVehicleId(id);
        v.setCustomerId(customers.get(customerCombo.getSelectedIndex() - 1).getCustomerId());
        v.setVehicleNumber(vNumberField.getText().trim().toUpperCase());
        v.setMake(makeField.getText().trim());
        v.setModel(modelField.getText().trim());
        try { v.setYear(Integer.parseInt(yearField.getText().trim())); } catch (Exception e) { v.setYear(0); }
        v.setColor(colorField.getText().trim());
        return v;
    }

    private void clearForm() {
        customerCombo.setSelectedIndex(0); vNumberField.setText(""); makeField.setText("");
        modelField.setText(""); yearField.setText(""); colorField.setText("");
        selectedId = -1; table.clearSelection();
        loadCustomers();
    }

    private VehicleDAO dao() { return vehicleDAO; }
}
