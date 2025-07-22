package dao.impl;

import dao.IProductDAO;
import model.Product;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements IProductDAO {
    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean insert(Product product) {
        String query = "INSERT INTO Product (name, brand, price, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm sản phẩm: " + e.getMessage());
            return false;
        }
    }
}

