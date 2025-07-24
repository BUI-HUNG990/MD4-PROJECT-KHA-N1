package dao.impl;

import dao.CustomerDAO;
import model.Customer;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Lớp triển khai CustomerDAO để thao tác trực tiếp với cơ sở dữ liệu liên quan đến bảng Customer.

public class CustomerDAOImpl implements CustomerDAO {
    // truy vấn kh từ bảng Customer
    @Override
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer"; // truy vân sql
        try (Connection conn = ConnectionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // thêm mới khách hàng vào cơ sở dữ liệu
    @Override
    public void add(Customer customer) {
        String sql = "INSERT INTO Customer(name, phone, email, address) VALUES (?, ?, ?, ?)"; // truy vấn sql
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // cập nhật thông tin khách hàng theo id
    @Override
    public void update(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?"; // truy vấn sql
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setInt(5, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // xoá khách hàng và toàn bộ hóa đơn liên quan, sử dụng transaction để đảm bảo toàn vẹn dữ liệu
    @Override
    public void delete(int id) {
        String deleteInvoicesSql = "DELETE FROM invoice WHERE customer_id = ?"; // truy vấn sql
        String deleteCustomerSql = "DELETE FROM customer WHERE id = ?"; // truy vấn sql

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteInvoicePs = conn.prepareStatement(deleteInvoicesSql);
                 PreparedStatement deleteCustomerPs = conn.prepareStatement(deleteCustomerSql)) {

                deleteInvoicePs.setInt(1, id);
                deleteInvoicePs.executeUpdate();

                deleteCustomerPs.setInt(1, id);
                deleteCustomerPs.executeUpdate();

                conn.commit();
                System.out.println("Xóa khách hàng và các hóa đơn liên quan thành công.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Lỗi khi xóa: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tìm kh theo id
    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM Customer WHERE id = ?";  // truy vấn sql
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
