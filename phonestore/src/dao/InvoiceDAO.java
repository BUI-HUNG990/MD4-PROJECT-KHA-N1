package dao;

import model.Invoice;

import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Date;
import java.util.List;

public interface InvoiceDAO {
    List<Invoice> getAllInvoices();

    List<Invoice> searchInvoicesByCustomerName(String name);

    List<Invoice> searchInvoicesByDate(Date date);

    void placeOrder(int customerId, int productId, int quantity);

    Map<String, Double> getRevenueBy(String type);


}