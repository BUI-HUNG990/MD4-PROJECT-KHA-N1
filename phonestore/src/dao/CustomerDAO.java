package dao;

import model.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> getAll();

    void add(Customer customer);

    void update(Customer customer);

    void delete(int id);

    Customer findById(int id);
}