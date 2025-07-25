package dao.impl;

import java.util.Map;
import java.util.LinkedHashMap;
import dao.InvoiceDAO;
import model.Invoice;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements InvoiceDAO {

    // Lấy tất cả hóa đơn
    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total_amount " +
                "FROM invoice i " +
                "JOIN customer c ON i.customer_id = c.id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
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

    // Tìm kiếm hóa đơn theo tên khách hàng (gần đúng)
    @Override
    public List<Invoice> searchInvoicesByCustomerName(String name) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total_amount " +
                "FROM invoice i " +
                "JOIN customer c ON i.customer_id = c.id " +
                "WHERE c.name LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm hóa đơn theo tên: " + e.getMessage());
        }
        return invoices;
    }

    // Tìm kiếm hóa đơn theo ngày (chỉ lấy phần ngày, bỏ giờ)
    @Override
    public List<Invoice> searchInvoicesByDate(Date date) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.id, i.customer_id, c.name AS customer_name, i.invoice_date, i.total_amount " +
                "FROM invoice i " +
                "JOIN customer c ON i.customer_id = c.id " +
                "WHERE DATE(i.invoice_date) = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm hóa đơn theo ngày: " + e.getMessage());
        }
        return invoices;
    }

    // Gọi stored procedure để tạo hóa đơn
    @Override
    public void placeOrder(int customerId, int productId, int quantity) {
        String sql = "{CALL place_order(?, ?, ?)}";
        try (Connection conn = DBUtil.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, customerId);
            cs.setInt(2, productId);
            cs.setInt(3, quantity);
            cs.execute();

            System.out.println("Đã thêm hóa đơn thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
        }
    }

    // Thống kê doanh thu theo ngày/tháng/năm
    @Override
    public Map<String, Double> getRevenueBy(String type) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = switch (type) {
            case "DATE" -> "SELECT DATE(invoice_date) AS time, SUM(total_amount) AS revenue " +
                    "FROM invoice GROUP BY DATE(invoice_date) ORDER BY DATE(invoice_date)";
            case "MONTH" -> "SELECT DATE_FORMAT(invoice_date, '%m/%Y') AS time, SUM(total_amount) AS revenue " +
                    "FROM invoice GROUP BY time ORDER BY STR_TO_DATE(CONCAT('01/', time), '%d/%m/%Y')";
            case "YEAR" -> "SELECT YEAR(invoice_date) AS time, SUM(total_amount) AS revenue " +
                    "FROM invoice GROUP BY time ORDER BY time";
            default -> null;
        };

        if (sql == null) return result;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("time"), rs.getDouble("revenue"));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thống kê doanh thu: " + e.getMessage());
        }
        return result;
    }
}
