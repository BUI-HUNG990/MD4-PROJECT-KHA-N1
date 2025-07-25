package model;

import java.sql.Date;

public class Invoice {
    private int id;
    private int customerId;
    private Date invoiceDate;
    private double totalAmount;
    private String customerName;


    public Invoice() {
    }
    public Invoice(int id, int customerId, String customerName, Date invoiceDate, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return String.format("%-8d | %-8d | %-20s | %-20s | %-12.2f",
                id, customerId, customerName, invoiceDate.toString(), totalAmount);
    }

}
