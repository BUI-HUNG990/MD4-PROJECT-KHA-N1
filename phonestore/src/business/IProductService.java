package business;

import model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    boolean addProduct(Product product);

    Product getProductById(int id);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    List<Product> searchProductByBrand(String keyword);

    List<Product> searchProductByPriceRange(double min, double max);

    List<Product> searchProductByStock(int minStock);


}

