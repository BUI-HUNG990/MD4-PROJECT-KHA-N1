
package business.impl;

import business.IProductService;
import dao.IProductDAO;
import dao.impl.ProductDAOImpl;
import model.Product;

import java.util.List;

// lớp triển khai các nghiệp vụ liên quan đến sản phẩm.
// là tầng trung gian xử lý logic giữa tầng presentation và tầng DAO.

public class ProductServiceImpl implements IProductService {
    // khởi tạo DAO để thao tác với bảng Product trong cơ sở dữ liệu
    private final IProductDAO productDAO = new ProductDAOImpl();

    // lấy danh sách tất cả sp trong hệ thống
    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    // thêm sp mới
    @Override
    public boolean addProduct(Product product) {
        return productDAO.insert(product);
    }

    // timd sp theo id
    @Override
    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    // cập nhật sp
    @Override
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    // xóa sp
    @Override
    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    // tìm kiếm theo thương hiệu
    @Override
    public List<Product> searchProductByBrand(String keyword) {
        return productDAO.searchByBrand(keyword);
    }

    // tìm sp theo khoảng giá
    @Override
    public List<Product> searchProductByPriceRange(double min, double max) {
        return productDAO.searchByPriceRange(min, max);
    }

    // tìm sp theo sl tồn kho
    @Override
    public List<Product> searchProductByStock(int minStock) {
        return productDAO.searchByStock(minStock);
    }


}

