package dao;

import model.Product;
import java.util.List;

public interface IProductDAO {
    List<Product> findAll();
    boolean insert(Product product);
}
