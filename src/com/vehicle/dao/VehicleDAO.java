package com.vehicle.dao;

import com.vehicle.model.Vehicle;
import com.vehicle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public boolean addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles (customer_id, vehicle_number, make, model, year, color) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getCustomerId());
            ps.setString(2, v.getVehicleNumber().toUpperCase());
            ps.setString(3, v.getMake());
            ps.setString(4, v.getModel());
            ps.setInt(5, v.getYear());
            ps.setString(6, v.getColor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateVehicle(Vehicle v) {
        String sql = "UPDATE vehicles SET customer_id=?, vehicle_number=?, make=?, model=?, year=?, color=? WHERE vehicle_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getCustomerId());
            ps.setString(2, v.getVehicleNumber().toUpperCase());
            ps.setString(3, v.getMake());
            ps.setString(4, v.getModel());
            ps.setInt(5, v.getYear());
            ps.setString(6, v.getColor());
            ps.setInt(7, v.getVehicleId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT v.*, c.full_name FROM vehicles v JOIN customers c ON v.customer_id=c.customer_id ORDER BY v.vehicle_number";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Vehicle v = mapRow(rs);
                v.setCustomerName(rs.getString("full_name"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Vehicle getVehicleByNumber(String vehicleNumber) {
        String sql = "SELECT v.*, c.full_name FROM vehicles v JOIN customers c ON v.customer_id=c.customer_id WHERE v.vehicle_number=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle v = mapRow(rs);
                v.setCustomerName(rs.getString("full_name"));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehicle> getVehiclesByCustomer(int customerId) {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT v.*, c.full_name FROM vehicles v JOIN customers c ON v.customer_id=c.customer_id WHERE v.customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle v = mapRow(rs);
                v.setCustomerName(rs.getString("full_name"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        return new Vehicle(
            rs.getInt("vehicle_id"),
            rs.getInt("customer_id"),
            rs.getString("vehicle_number"),
            rs.getString("make"),
            rs.getString("model"),
            rs.getInt("year"),
            rs.getString("color")
        );
    }
}
