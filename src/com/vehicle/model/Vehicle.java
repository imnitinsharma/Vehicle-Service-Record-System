package com.vehicle.model;

public class Vehicle {
    private int vehicleId;
    private int customerId;
    private String vehicleNumber;
    private String make;
    private String model;
    private int year;
    private String color;
    private String customerName; // for display

    public Vehicle() {}

    public Vehicle(int vehicleId, int customerId, String vehicleNumber,
                   String make, String model, int year, String color) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.vehicleNumber = vehicleNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    public int getVehicleId()           { return vehicleId; }
    public int getCustomerId()           { return customerId; }
    public String getVehicleNumber()     { return vehicleNumber; }
    public String getMake()              { return make; }
    public String getModel()             { return model; }
    public int getYear()                 { return year; }
    public String getColor()             { return color; }
    public String getCustomerName()      { return customerName; }

    public void setVehicleId(int id)         { this.vehicleId = id; }
    public void setCustomerId(int id)        { this.customerId = id; }
    public void setVehicleNumber(String n)   { this.vehicleNumber = n; }
    public void setMake(String m)            { this.make = m; }
    public void setModel(String m)           { this.model = m; }
    public void setYear(int y)               { this.year = y; }
    public void setColor(String c)           { this.color = c; }
    public void setCustomerName(String n)    { this.customerName = n; }

    @Override
    public String toString() { return vehicleNumber + " - " + make + " " + model; }
}
