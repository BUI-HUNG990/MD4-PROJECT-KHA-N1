package business;

import model.Invoice;

import java.sql.Date;
import java.util.List;

public interface InvoiceService {
    List<Invoice> getAllInvoices();
    List<Invoice> searchInvoicesByCustomerName(String name);
    List<Invoice> searchInvoicesByDate(Date date);
}

