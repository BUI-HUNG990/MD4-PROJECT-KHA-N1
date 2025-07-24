package business.impl;

import business.InvoiceService;
import dao.InvoiceDAO;
import dao.impl.InvoiceDAOImpl;
import model.Invoice;

import java.sql.Date;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceDAO invoiceDAO = new InvoiceDAOImpl();

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }

    @Override
    public List<Invoice> searchInvoicesByCustomerName(String name) {
        return invoiceDAO.searchInvoicesByCustomerName(name);
    }

    @Override
    public List<Invoice> searchInvoicesByDate(Date date) {
        return invoiceDAO.searchInvoicesByDate(date);
    }
}

