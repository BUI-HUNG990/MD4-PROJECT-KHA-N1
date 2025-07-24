package dao.impl;

import dao.IProductDAO;
import model.Product;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements IProductDAO {

    // Lấy toàn bộ sản phẩm từ bảng Product
    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            // Duyệt kết quả trả về và ánh xạ vào đối tượng Product
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

    // Thêm một sản phẩm mới vào bảng Product
    @Override
    public boolean insert(Product product) {
        String query = "INSERT INTO Product (name, brand, price, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            // Gán giá trị thuộc tính từ đối tượng Product
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            // Trả về true nếu thêm thành công
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm sản phẩm: " + e.getMessage());
            return false;
        }
    }

    // Tìm sản phẩm theo ID
    @Override
    public Product findById(int id) {
        String sql = "SELECT * FROM Product WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            // Nếu có kết quả thì tạo đối tượng Product từ dữ liệu trả về
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm sản phẩm theo ID: " + e.getMessage());
        }
        return null;
    }

    // Cập nhật thông tin sản phẩm
    @Override
    public boolean update(Product product) {
        String sql = "UPDATE Product SET name = ?, brand = ?, price = ?, stock = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Gán các giá trị cập nhật
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            return false;
        }
    }

    // Xóa sản phẩm theo ID
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Product WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            return false;
        }
    }

    // Tìm sản phẩm theo tên thương hiệu
    @Override
    public List<Product> searchByBrand(String keyword) {
        List<Product> result = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE brand LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm sản phẩm theo thương hiệu: " + e.getMessage());
        }
        return result;
    }

    // Tìm sản phẩm theo khoảng giá
    @Override
    public List<Product> searchByPriceRange(double minPrice, double maxPrice) {
        List<Product> result = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE price BETWEEN ? AND ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm sản phẩm " + e.getMessage());
        }
        return result;
    }

    // Tìm sản phẩm có số lượng tồn kho lớn hơn hoặc bằng giá trị chỉ định
    @Override
    public List<Product> searchByStock(int minStock) {
        List<Product> result = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE stock >= ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minStock);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("lỗi tìm kiếm tồn kho: " + e.getMessage());
        }
        return result;
    }
}
