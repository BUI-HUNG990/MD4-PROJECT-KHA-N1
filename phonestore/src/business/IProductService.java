package business;

import model.Product;
import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    boolean addProduct(Product product);
}

