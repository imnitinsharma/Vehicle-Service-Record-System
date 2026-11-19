package com.vehicle.ui;

import com.vehicle.util.DBConnection;

import javax.swing.*;
import java.awt.*;

/**
 * Vehicle Service Record System - Main Window
 * Author: Nitin Kumar Sharma
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("🔧 Vehicle Service Record System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        initComponents();
        applyTheme();
    }

    private void initComponents() {
        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 114));
        header.setPreferredSize(new Dimension(0, 65));

        JLabel titleLabel = new JLabel("  🔧 Vehicle Service Record System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel("Workshop Management Solution  ");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(180, 220, 255));
        subLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(subLabel, BorderLayout.EAST);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.addTab("🔍 Search Service History", new SearchPanel());
        tabbedPane.addTab("👤 Customers", new CustomerPanel());
        tabbedPane.addTab("🚗 Vehicles", new VehiclePanel());
        tabbedPane.addTab("🛠 Service Records", new ServicePanel());

        // Status bar
        JLabel statusBar = new JLabel("  ✅ Connected to database — Vehicle Service System v1.0  |  Author: Nitin Kumar Sharma");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setOpaque(true);
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void applyTheme() {
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(new Color(30, 60, 114));
                } else {
                    g.setColor(new Color(220, 230, 245));
                }
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Check DB connection
        if (!DBConnection.testConnection()) {
            JOptionPane.showMessageDialog(null,
                "❌ Cannot connect to MySQL database!\n\n" +
                "Please check:\n" +
                "1. MySQL is running\n" +
                "2. Database 'vehicle_service_db' exists\n" +
                "3. Username/password in DBConnection.java\n" +
                "4. mysql-connector-java.jar is in classpath",
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
