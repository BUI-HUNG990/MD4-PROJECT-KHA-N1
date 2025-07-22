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
}

