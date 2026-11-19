=====================================================
 VEHICLE SERVICE RECORD SYSTEM
 Author     : Nitin Kumar Sharma
 College    : Poornima Institute of Engineering & Technology, Jaipur
 Technology : Java Swing + JDBC + MySQL
 SDG        : SDG 9 – Industry, Innovation and Infrastructure
=====================================================

── WHAT THIS PROJECT DOES ──────────────────────────
 ✅ Add / Update / Delete Customers
 ✅ Add / Update / Delete Vehicles (linked to customers)
 ✅ Add / Update / Delete Service Records
 ✅ Search full service history by Vehicle Number
 ✅ View total cost per vehicle
 ✅ Status color-coding (Pending / In Progress / Completed)

── REQUIREMENTS ────────────────────────────────────
 • JDK 8 or above
 • MySQL Server (running on localhost:3306)
 • mysql-connector-j-8.0.33.jar  ← place in /lib folder

── SETUP STEPS ─────────────────────────────────────

STEP 1 — Set up the Database
  1. Open MySQL Workbench or run MySQL command line
  2. Run the file:  database_setup.sql
     Command:  mysql -u root -p < database_setup.sql
  This creates the database, tables, and sample data.

STEP 2 — Set your MySQL password
  Open:  src/com/vehicle/util/DBConnection.java
  Change this line to your MySQL root password:
      private static final String PASSWORD = "";

STEP 3 — Add the MySQL JDBC Driver
  Download from: https://dev.mysql.com/downloads/connector/j/
  Choose: Platform Independent → ZIP archive
  Extract and copy:  mysql-connector-j-8.0.33.jar
  Paste into the:   lib/   folder inside this project

STEP 4 — Compile and Run

  Windows:
      Double-click  run.bat
      OR in Command Prompt:   run.bat

  Linux / Mac:
      chmod +x run.sh
      ./run.sh

  In an IDE (Eclipse / IntelliJ):
      - Add mysql-connector-j-8.0.33.jar to Build Path
      - Run MainFrame.java

── PROJECT STRUCTURE ───────────────────────────────
 VehicleServiceSystem/
 ├── src/
 │   └── com/vehicle/
 │       ├── model/
 │       │   ├── Customer.java
 │       │   ├── Vehicle.java
 │       │   └── ServiceRecord.java
 │       ├── dao/
 │       │   ├── CustomerDAO.java
 │       │   ├── VehicleDAO.java
 │       │   └── ServiceRecordDAO.java
 │       ├── ui/
 │       │   ├── MainFrame.java       ← Main window
 │       │   ├── SearchPanel.java     ← Search by vehicle number
 │       │   ├── CustomerPanel.java
 │       │   ├── VehiclePanel.java
 │       │   └── ServicePanel.java
 │       └── util/
 │           └── DBConnection.java
 ├── lib/
 │   └── mysql-connector-j-8.0.33.jar  ← You add this
 ├── database_setup.sql
 ├── run.bat    (Windows)
 ├── run.sh     (Linux/Mac)
 └── README.txt

── PRESENTATION POINTS ─────────────────────────────
 1. This is a workshop management solution for local mechanics.
 2. Uses 3-tier architecture: UI → DAO → Database
 3. CRUD operations on 3 related entities (Customer, Vehicle, Service)
 4. Core feature: retrieve complete service history by just entering a vehicle number
 5. Supports SDG 9 by digitizing local workshop operations, reducing paperwork
    and improving service transparency for customers.

=====================================================
