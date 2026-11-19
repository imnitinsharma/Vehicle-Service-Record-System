package com.vehicle.ui;

import com.vehicle.dao.ServiceRecordDAO;
import com.vehicle.dao.VehicleDAO;
import com.vehicle.model.ServiceRecord;
import com.vehicle.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {

    private JTextField searchField;
    private JButton searchBtn;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel vehicleInfoLabel;
    private JLabel totalCostLabel;

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private ServiceRecordDAO serviceDAO = new ServiceRecordDAO();

    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        // ── Search Bar ──────────────────────────────────────────
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setBackground(new Color(240, 245, 255));
        searchBar.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        JLabel lbl = new JLabel("🔍  Enter Vehicle Number:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        searchField = new JTextField(16);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setPreferredSize(new Dimension(200, 34));
        searchField.setText("e.g. RJ14CD1234");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().startsWith("e.g.")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
        });

        searchBtn = new JButton("Search History");
        searchBtn.setBackground(new Color(30, 60, 114));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBtn.setPreferredSize(new Dimension(150, 34));
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton clearBtn = new JButton("Clear");
        clearBtn.setPreferredSize(new Dimension(80, 34));
        clearBtn.setFocusPainted(false);

        searchBar.add(lbl);
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(clearBtn);

        // ── Vehicle Info Label ───────────────────────────────────
        vehicleInfoLabel = new JLabel("  Enter a vehicle number and click Search to view service history.");
        vehicleInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        vehicleInfoLabel.setForeground(new Color(80, 80, 80));
        vehicleInfoLabel.setBorder(BorderFactory.createEmptyBorder(6, 4, 2, 4));

        // ── Table ────────────────────────────────────────────────
        String[] cols = {"#", "Service Date", "Service Type", "Description", "Mechanic", "Cost (₹)", "Status", "Next Service"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultTable.setRowHeight(26);
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        resultTable.getTableHeader().setBackground(new Color(30, 60, 114));
        resultTable.getTableHeader().setForeground(Color.WHITE);
        resultTable.setSelectionBackground(new Color(190, 220, 255));
        resultTable.setGridColor(new Color(220, 225, 235));
        resultTable.setShowGrid(true);

        // Column widths
        int[] widths = {35, 95, 130, 200, 110, 90, 100, 110};
        for (int i = 0; i < widths.length; i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Status column coloring
        resultTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));

        // ── Total Cost Label ─────────────────────────────────────
        totalCostLabel = new JLabel("  ");
        totalCostLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalCostLabel.setForeground(new Color(30, 60, 114));
        totalCostLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // ── Layout ───────────────────────────────────────────────
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(searchBar, BorderLayout.NORTH);
        topPanel.add(vehicleInfoLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(totalCostLabel, BorderLayout.SOUTH);

        // ── Actions ───────────────────────────────────────────────
        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        clearBtn.addActionListener(e -> {
            searchField.setText("e.g. RJ14CD1234");
            searchField.setForeground(Color.GRAY);
            tableModel.setRowCount(0);
            vehicleInfoLabel.setText("  Enter a vehicle number and click Search.");
            totalCostLabel.setText("  ");
        });
    }

    private void performSearch() {
        String query = searchField.getText().trim().toUpperCase();
        if (query.isEmpty() || query.startsWith("E.G.")) {
            JOptionPane.showMessageDialog(this, "Please enter a vehicle number.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        Vehicle v = vehicleDAO.getVehicleByNumber(query);
        if (v == null) {
            vehicleInfoLabel.setText("  ❌  Vehicle '" + query + "' not found in the system.");
            vehicleInfoLabel.setForeground(new Color(180, 0, 0));
            totalCostLabel.setText("  ");
            return;
        }

        vehicleInfoLabel.setText(String.format(
            "  ✅  %s  |  %s %s (%d)  |  Color: %s  |  Owner: %s",
            v.getVehicleNumber(), v.getMake(), v.getModel(), v.getYear(), v.getColor(), v.getCustomerName()
        ));
        vehicleInfoLabel.setForeground(new Color(0, 100, 0));
        vehicleInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        List<ServiceRecord> records = serviceDAO.getServiceHistoryByVehicleNumber(query);
        if (records.isEmpty()) {
            vehicleInfoLabel.setText(vehicleInfoLabel.getText() + "  — No service records found.");
            totalCostLabel.setText("  ");
            return;
        }

        double total = 0;
        int sno = 1;
        for (ServiceRecord sr : records) {
            tableModel.addRow(new Object[]{
                sno++,
                sr.getServiceDate(),
                sr.getServiceType(),
                sr.getDescription(),
                sr.getMechanicName(),
                String.format("%.2f", sr.getCost()),
                sr.getStatus(),
                sr.getNextServiceDate() != null ? sr.getNextServiceDate() : "—"
            });
            total += sr.getCost();
        }

        totalCostLabel.setText(String.format(
            "  📊  Total Records: %d   |   Total Service Cost: ₹ %.2f", records.size(), total
        ));
    }
}
