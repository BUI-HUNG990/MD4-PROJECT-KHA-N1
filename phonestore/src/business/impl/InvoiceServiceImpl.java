package business.impl;

import java.util.Map;
import java.util.LinkedHashMap;

import business.InvoiceService;
import dao.InvoiceDAO;
import dao.impl.InvoiceDAOImpl;
import model.Invoice;

import java.sql.Date;
import java.util.List;

// lớp triển khai các nghiệp vụ liên quan đến hóa đơn (Invoice).
// đây là tầng trung gian xử lý logic trước khi thao tác dữ liệu qua DAO.

public class InvoiceServiceImpl implements InvoiceService {
    // khởi tạo DAO để thao tác với cơ sở dữ liệu
    private final InvoiceDAO invoiceDAO = new InvoiceDAOImpl();

    // lấy hết danh sách hđ
    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }

    // tìm hóa đơn theo tên kh
    @Override
    public List<Invoice> searchInvoicesByCustomerName(String name) {
        return invoiceDAO.searchInvoicesByCustomerName(name);
    }

    // tìm hđ theo ngày tạo
    @Override
    public List<Invoice> searchInvoicesByDate(Date date) {
        return invoiceDAO.searchInvoicesByDate(date);
    }

    // tạo đơn hàng mới
    @Override
    public void placeOrder(int customerId, int productId, int quantity) {
        invoiceDAO.placeOrder(customerId, productId, quantity);
    }

    // truy vấn doanh thu theo ngày
    @Override
    public Map<String, Double> getRevenueByDate() {
        return invoiceDAO.getRevenueBy("DATE");
    }

    // truy vấn doanh thu theo tháng
    @Override
    public Map<String, Double> getRevenueByMonth() {
        return invoiceDAO.getRevenueBy("MONTH");
    }

    // truy vấn doanh thu theo năm
    @Override
    public Map<String, Double> getRevenueByYear() {
        return invoiceDAO.getRevenueBy("YEAR");
    }
}
