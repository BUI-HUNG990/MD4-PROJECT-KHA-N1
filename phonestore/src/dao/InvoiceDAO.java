package dao;

import model.Invoice;

import java.sql.Date;
import java.util.List;

public interface InvoiceDAO {
    List<Invoice> getAllInvoices();
    List<Invoice> searchInvoicesByCustomerName(String name);
    List<Invoice> searchInvoicesByDate(Date date);

}
