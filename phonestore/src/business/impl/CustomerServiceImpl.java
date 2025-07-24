
package business.impl;

import business.CustomerService;
import dao.CustomerDAO;
import dao.impl.CustomerDAOImpl;
import model.Customer;

import java.util.List;
// lớp triển khai các nghiệp vụ liên quan đến khách hàng.
// tầng này là cầu nối giữa tầng presentation (giao diện) và tầng DAO (truy xuất cơ sở dữ liệu).

public class CustomerServiceImpl implements CustomerService {
    // tạo instance của DAO để thực hiện thao tác cơ sở dữ liệu với bảng Customer
    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    // lấy danh sách kh
    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.getAll();
    }

    // thêm kh mới vào hệ thống
    @Override
    public void addCustomer(Customer customer) {
        customerDAO.add(customer);
    }

    // cập nhật tt kh
    @Override
    public void updateCustomer(Customer customer) {
        customerDAO.update(customer);
    }

    // xóa kh theo id
    @Override
    public void deleteCustomer(int id) {
        customerDAO.delete(id);
    }

    // tìm kh theo id
    @Override
    public Customer getCustomerById(int id) {
        return customerDAO.findById(id);
    }
}

