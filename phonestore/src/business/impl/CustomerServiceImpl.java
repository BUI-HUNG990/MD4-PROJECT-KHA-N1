package business.impl;

import business.CustomerService;
import dao.CustomerDAO;
import dao.impl.CustomerDAOImpl;
import model.Customer;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.getAll();
    }

    @Override
    public void addCustomer(Customer customer) {
        customerDAO.add(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerDAO.update(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        customerDAO.delete(id);
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerDAO.findById(id);
    }
}

