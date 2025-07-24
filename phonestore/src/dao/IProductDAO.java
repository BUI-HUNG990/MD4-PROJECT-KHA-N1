package dao;

import model.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> findAll();

    boolean insert(Product product);

    Product findById(int id);

    boolean update(Product product);

    boolean delete(int id);

    List<Product> searchByBrand(String keyword);

    List<Product> searchByPriceRange(double minPrice, double maxPrice);

    List<Product> searchByStock(int minStock);


}
