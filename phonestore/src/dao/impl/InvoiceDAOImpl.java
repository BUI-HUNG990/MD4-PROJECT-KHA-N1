package dao.impl;

import dao.InvoiceDAO;
import model.Invoice;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements InvoiceDAO {

    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                list.add(invoice);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Invoice> searchInvoicesByCustomerName(String name) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.* FROM invoice i JOIN customer c ON i.customer_id = c.id WHERE c.name LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                list.add(invoice);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi tìm hóa đơn theo tên: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Invoice> searchInvoicesByDate(Date date) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE invoice_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                list.add(invoice);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi tìm hóa đơn theo ngày: " + e.getMessage());
        }
        return list;
    }
}

