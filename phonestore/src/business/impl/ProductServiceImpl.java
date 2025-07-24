package business.impl;

import business.IProductService;
import dao.IProductDAO;
import dao.impl.ProductDAOImpl;
import model.Product;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    private final IProductDAO productDAO = new ProductDAOImpl();

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public boolean addProduct(Product product) {
        return productDAO.insert(product);
    }

    @Override
    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    @Override
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    @Override
    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    @Override
    public List<Product> searchProductByBrand(String keyword) {
        return productDAO.searchByBrand(keyword);
    }

    @Override
    public List<Product> searchProductByPriceRange(double min, double max) {
        return productDAO.searchByPriceRange(min, max);
    }

    @Override
    public List<Product> searchProductByStock(int minStock) {
        return productDAO.searchByStock(minStock);
    }


}

