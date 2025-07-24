package dao.impl;
import java.util.Map;
import java.util.LinkedHashMap;
import dao.InvoiceDAO;
import model.Invoice;
import utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Triển khai các phương thức truy xuất và xử lý dữ liệu liên quan đến hóa đơn Invoice

public class InvoiceDAOImpl implements InvoiceDAO {

    // Lấy danh sách tất cả hóa đơn từ cơ sở dữ liệu
    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice"; // Truy vấn sql
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

    // Tìm kiếm hóa đơn theo tên khách hàng (có thể tìm gần đúng).
    // Dùng JOIN giữa bảng invoice và customer.
    @Override
    public List<Invoice> searchInvoicesByCustomerName(String name) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.* FROM invoice i JOIN customer c ON i.customer_id = c.id WHERE c.name LIKE ?"; // truy vấn sql
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

    // Tìm hóa đơn theo đúng ngày lập
    @Override
    public List<Invoice> searchInvoicesByDate(Date date) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE DATE(invoice_date) = ?";; // truy vấn sql
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

    // Gọi stored procedure `place_order` để thêm hóa đơn mới.
    @Override
    public void placeOrder(int customerId, int productId, int quantity) {
        String sql = "{CALL place_order(?, ?, ?)}"; // truy vấn sql
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

    // Truy vấn thống kê doanh thu theo ngày, tháng hoặc năm
    @Override
    public Map<String, Double> getRevenueBy(String type) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = switch (type) {
            case "DATE" -> "SELECT invoice_date AS time, SUM(total_amount) AS revenue FROM invoice GROUP BY invoice_date ORDER BY invoice_date";
            case "MONTH" -> "SELECT DATE_FORMAT(invoice_date, '%m/%Y') AS time, SUM(total_amount) AS revenue FROM invoice GROUP BY time ORDER BY STR_TO_DATE(CONCAT('01/', time), '%d/%m/%Y')";
            case "YEAR" -> "SELECT YEAR(invoice_date) AS time, SUM(total_amount) AS revenue FROM invoice GROUP BY time ORDER BY time";
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
