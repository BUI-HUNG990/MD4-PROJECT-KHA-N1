
package business;

import model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    Customer getCustomerById(int id);
}
