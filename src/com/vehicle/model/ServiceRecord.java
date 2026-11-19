package com.vehicle.model;

import java.sql.Date;

public class ServiceRecord {
    private int serviceId;
    private int vehicleId;
    private Date serviceDate;
    private String serviceType;
    private String description;
    private String mechanicName;
    private double cost;
    private String status;
    private Date nextServiceDate;
    private String vehicleNumber; // for display

    public ServiceRecord() {}

    public ServiceRecord(int serviceId, int vehicleId, Date serviceDate,
                         String serviceType, String description,
                         String mechanicName, double cost, String status,
                         Date nextServiceDate) {
        this.serviceId = serviceId;
        this.vehicleId = vehicleId;
        this.serviceDate = serviceDate;
        this.serviceType = serviceType;
        this.description = description;
        this.mechanicName = mechanicName;
        this.cost = cost;
        this.status = status;
        this.nextServiceDate = nextServiceDate;
    }

    public int getServiceId()            { return serviceId; }
    public int getVehicleId()            { return vehicleId; }
    public Date getServiceDate()         { return serviceDate; }
    public String getServiceType()       { return serviceType; }
    public String getDescription()       { return description; }
    public String getMechanicName()      { return mechanicName; }
    public double getCost()              { return cost; }
    public String getStatus()            { return status; }
    public Date getNextServiceDate()     { return nextServiceDate; }
    public String getVehicleNumber()     { return vehicleNumber; }

    public void setServiceId(int id)             { this.serviceId = id; }
    public void setVehicleId(int id)             { this.vehicleId = id; }
    public void setServiceDate(Date d)           { this.serviceDate = d; }
    public void setServiceType(String t)         { this.serviceType = t; }
    public void setDescription(String d)         { this.description = d; }
    public void setMechanicName(String m)        { this.mechanicName = m; }
    public void setCost(double c)                { this.cost = c; }
    public void setStatus(String s)              { this.status = s; }
    public void setNextServiceDate(Date d)       { this.nextServiceDate = d; }
    public void setVehicleNumber(String n)       { this.vehicleNumber = n; }
}
