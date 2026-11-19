package com.vehicle.ui;

import com.vehicle.dao.CustomerDAO;
import com.vehicle.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    private JTextField nameField, phoneField, emailField, addressField, searchField;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn, searchBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedId = -1;

    private CustomerDAO dao = new CustomerDAO();

    public CustomerPanel() {
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
            BorderFactory.createLineBorder(new Color(180, 200, 230)), "Customer Details",
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(30, 60, 114)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 8, 5, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        nameField    = new JTextField(18);
        phoneField   = new JTextField(18);
        emailField   = new JTextField(18);
        addressField = new JTextField(18);

        String[][] fields = {{"Full Name *", ""}, {"Phone *", ""}, {"Email", ""}, {"Address", ""}};
        JTextField[] inputs = {nameField, phoneField, emailField, addressField};

        for (int i = 0; i < fields.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            JLabel l = new JLabel(fields[i][0] + ":"); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            formCard.add(l, gc);
            gc.gridx = 1; gc.weightx = 1;
            inputs[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            formCard.add(inputs[i], gc);
        }

        // Buttons
        addBtn    = makeBtn("➕ Add",    new Color(34, 139, 34));
        updateBtn = makeBtn("✏ Update", new Color(70, 130, 180));
        deleteBtn = makeBtn("🗑 Delete", new Color(180, 50, 50));
        clearBtn  = makeBtn("✖ Clear",  new Color(130, 130, 130));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnRow.setBackground(new Color(245, 248, 255));
        btnRow.add(addBtn); btnRow.add(updateBtn); btnRow.add(deleteBtn); btnRow.add(clearBtn);

        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2;
        formCard.add(btnRow, gc);

        // ── Search Row ───────────────────────────────────────────
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchRow.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchBtn = makeBtn("🔍 Search", new Color(30, 60, 114));
        JButton showAllBtn = makeBtn("Show All", new Color(90, 90, 90));
        searchRow.add(new JLabel("Search (name/phone):"));
        searchRow.add(searchField); searchRow.add(searchBtn); searchRow.add(showAllBtn);

        // ── Table ────────────────────────────────────────────────
        String[] cols = {"ID", "Full Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);
        int[] widths = {40, 160, 110, 160, 220};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 4));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(searchRow, BorderLayout.NORTH);
        bottomPanel.add(sp, BorderLayout.CENTER);

        add(formCard, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        // ── Listeners ────────────────────────────────────────────
        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        clearBtn.addActionListener(e -> clearForm());
        searchBtn.addActionListener(e -> searchCustomers());
        showAllBtn.addActionListener(e -> loadTable());
        searchField.addActionListener(e -> searchCustomers());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedId = (int) tableModel.getValueAt(row, 0);
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                phoneField.setText(tableModel.getValueAt(row, 2).toString());
                emailField.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                addressField.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
            }
        });
    }

    private void addCustomer() {
        if (!validateForm()) return;
        Customer c = buildCustomer(0);
        if (dao.addCustomer(c)) {
            JOptionPane.showMessageDialog(this, "✅ Customer added successfully!");
            clearForm(); loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Select a customer to update."); return; }
        if (!validateForm()) return;
        Customer c = buildCustomer(selectedId);
        if (dao.updateCustomer(c)) {
            JOptionPane.showMessageDialog(this, "✅ Customer updated successfully!");
            clearForm(); loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Select a customer to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this customer and ALL their vehicles & records?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deleteCustomer(selectedId)) {
                JOptionPane.showMessageDialog(this, "✅ Customer deleted!");
                clearForm(); loadTable();
            }
        }
    }

    private void searchCustomers() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadTable(); return; }
        populateTable(dao.searchCustomers(kw));
    }

    private void loadTable() {
        populateTable(dao.getAllCustomers());
    }

    private void populateTable(List<Customer> list) {
        tableModel.setRowCount(0);
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(), c.getFullName(), c.getPhone(), c.getEmail(), c.getAddress()
            });
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name is required."); return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone is required."); return false;
        }
        return true;
    }

    private Customer buildCustomer(int id) {
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setFullName(nameField.getText().trim());
        c.setPhone(phoneField.getText().trim());
        c.setEmail(emailField.getText().trim());
        c.setAddress(addressField.getText().trim());
        return c;
    }

    private void clearForm() {
        nameField.setText(""); phoneField.setText(""); emailField.setText(""); addressField.setText("");
        searchField.setText(""); selectedId = -1; table.clearSelection();
    }

    static JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(110, 30));
        return b;
    }

    static void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(25);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(30, 60, 114));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(190, 220, 255));
        t.setGridColor(new Color(220, 225, 235));
        t.setShowGrid(true);
    }
}
