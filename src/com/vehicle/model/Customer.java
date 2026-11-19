package com.vehicle.model;

public class Customer {
    private int customerId;
    private String fullName;
    private String phone;
    private String email;
    private String address;

    public Customer() {}

    public Customer(int customerId, String fullName, String phone, String email, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getCustomerId()         { return customerId; }
    public String getFullName()         { return fullName; }
    public String getPhone()            { return phone; }
    public String getEmail()            { return email; }
    public String getAddress()          { return address; }

    public void setCustomerId(int id)   { this.customerId = id; }
    public void setFullName(String n)   { this.fullName = n; }
    public void setPhone(String p)      { this.phone = p; }
    public void setEmail(String e)      { this.email = e; }
    public void setAddress(String a)    { this.address = a; }

    @Override
    public String toString() { return customerId + " - " + fullName + " (" + phone + ")"; }
}
