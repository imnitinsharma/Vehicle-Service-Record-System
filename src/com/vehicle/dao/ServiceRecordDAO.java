package com.vehicle.dao;

import com.vehicle.model.ServiceRecord;
import com.vehicle.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRecordDAO {

    public boolean addServiceRecord(ServiceRecord sr) {
        String sql = "INSERT INTO service_records (vehicle_id, service_date, service_type, description, mechanic_name, cost, status, next_service_date) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sr.getVehicleId());
            ps.setDate(2, sr.getServiceDate());
            ps.setString(3, sr.getServiceType());
            ps.setString(4, sr.getDescription());
            ps.setString(5, sr.getMechanicName());
            ps.setDouble(6, sr.getCost());
            ps.setString(7, sr.getStatus());
            ps.setDate(8, sr.getNextServiceDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateServiceRecord(ServiceRecord sr) {
        String sql = "UPDATE service_records SET service_date=?, service_type=?, description=?, mechanic_name=?, cost=?, status=?, next_service_date=? WHERE service_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, sr.getServiceDate());
            ps.setString(2, sr.getServiceType());
            ps.setString(3, sr.getDescription());
            ps.setString(4, sr.getMechanicName());
            ps.setDouble(5, sr.getCost());
            ps.setString(6, sr.getStatus());
            ps.setDate(7, sr.getNextServiceDate());
            ps.setInt(8, sr.getServiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteServiceRecord(int serviceId) {
        String sql = "DELETE FROM service_records WHERE service_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ServiceRecord> getServiceHistoryByVehicleNumber(String vehicleNumber) {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT sr.*, v.vehicle_number FROM service_records sr " +
                     "JOIN vehicles v ON sr.vehicle_id = v.vehicle_id " +
                     "WHERE v.vehicle_number=? ORDER BY sr.service_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleNumber.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ServiceRecord sr = mapRow(rs);
                sr.setVehicleNumber(rs.getString("vehicle_number"));
                list.add(sr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ServiceRecord> getServicesByVehicleId(int vehicleId) {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT sr.*, v.vehicle_number FROM service_records sr " +
                     "JOIN vehicles v ON sr.vehicle_id = v.vehicle_id " +
                     "WHERE sr.vehicle_id=? ORDER BY sr.service_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ServiceRecord sr = mapRow(rs);
                sr.setVehicleNumber(rs.getString("vehicle_number"));
                list.add(sr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ServiceRecord> getAllServiceRecords() {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT sr.*, v.vehicle_number FROM service_records sr " +
                     "JOIN vehicles v ON sr.vehicle_id = v.vehicle_id ORDER BY sr.service_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ServiceRecord sr = mapRow(rs);
                sr.setVehicleNumber(rs.getString("vehicle_number"));
                list.add(sr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ServiceRecord mapRow(ResultSet rs) throws SQLException {
        return new ServiceRecord(
            rs.getInt("service_id"),
            rs.getInt("vehicle_id"),
            rs.getDate("service_date"),
            rs.getString("service_type"),
            rs.getString("description"),
            rs.getString("mechanic_name"),
            rs.getDouble("cost"),
            rs.getString("status"),
            rs.getDate("next_service_date")
        );
    }
}
