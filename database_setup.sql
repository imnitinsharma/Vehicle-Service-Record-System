-- ============================================
-- Vehicle Service Record System - Database Setup
-- Author: Nitin Kumar Sharma
-- ============================================

CREATE DATABASE IF NOT EXISTS vehicle_service_db;
USE vehicle_service_db;

-- Customer Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicle Table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_number VARCHAR(20) NOT NULL UNIQUE,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT,
    color VARCHAR(30),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Service Records Table
CREATE TABLE IF NOT EXISTS service_records (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    service_date DATE NOT NULL,
    service_type VARCHAR(100) NOT NULL,
    description TEXT,
    mechanic_name VARCHAR(100),
    cost DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('Pending','In Progress','Completed') DEFAULT 'Pending',
    next_service_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE CASCADE
);

-- Sample Data
INSERT INTO customers (full_name, phone, email, address) VALUES
('Rahul Sharma', '9876543210', 'rahul@email.com', 'Jaipur, Rajasthan'),
('Priya Singh', '9988776655', 'priya@email.com', 'Jaipur, Rajasthan');

INSERT INTO vehicles (customer_id, vehicle_number, make, model, year, color) VALUES
(1, 'RJ14CD1234', 'Maruti', 'Swift', 2020, 'White'),
(2, 'RJ14EF5678', 'Honda', 'City', 2019, 'Silver');

INSERT INTO service_records (vehicle_id, service_date, service_type, description, mechanic_name, cost, status, next_service_date) VALUES
(1, '2024-01-15', 'Oil Change', 'Engine oil and filter replaced', 'Ramesh Kumar', 850.00, 'Completed', '2024-07-15'),
(1, '2024-06-10', 'General Service', 'Full service with brake check', 'Suresh Mehta', 2500.00, 'Completed', '2024-12-10'),
(2, '2024-03-20', 'Tyre Rotation', 'All 4 tyres rotated and balanced', 'Ramesh Kumar', 600.00, 'Completed', '2024-09-20');

SELECT 'Database setup complete!' AS status;
