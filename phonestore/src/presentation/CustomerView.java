package presentation;

import business.CustomerService;
import business.impl.CustomerServiceImpl;
import model.Customer;
import utils.InputUtil;

import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private final CustomerService customerService = new CustomerServiceImpl();
    private final Scanner sc = new Scanner(System.in);

    public void displayCustomerMenu() {
        int choice;
        do {
            System.out.println("======== QUẢN LÝ KHÁCH HÀNG ========");
            System.out.println("1. Hiển thị danh sách khách hàng");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("3. Cập nhật thông tin khách hàng");
            System.out.println("4. Xóa khách hàng theo ID");
            System.out.println("5. Quay lại menu chính");
            System.out.println("====================================");

            choice = InputUtil.getInt("Nhập lựa chọn: ");
            switch (choice) {
                case 1:
                    showAllCustomers();
                    break;
                case 2:
                    addNewCustomer();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    System.out.println("Trở lại menu chính");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập lại");
            }
        } while (choice != 5);
    }

    private void showAllCustomers() {
        List<Customer> list = customerService.getAllCustomers();
        if (list.isEmpty()) {
            System.out.println("Danh sách khách hàng trống.");
        } else {
            System.out.println("====== DANH SÁCH KHÁCH HÀNG ======");
            list.forEach(System.out::println);
        }
    }

    private void addNewCustomer() {
        System.out.println("===== Thêm khách hàng mới =====");
        String name = InputUtil.getString("Nhập tên khách hàng: ");
        String phone = InputUtil.getString("Nhập số điện thoại: ");
        String email = InputUtil.getString("Nhập email: ");
        String address = InputUtil.getString("Nhập địa chỉ: ");

        Customer customer = new Customer(0, name, phone, email, address);
        customerService.addCustomer(customer);
        System.out.println("Thêm khách hàng thành công.");
    }

    private void updateCustomer() {
        int id = InputUtil.getInt("Nhập ID khách hàng cần cập nhật: ");
        Customer existing = customerService.getCustomerById(id);
        if (existing == null) {
            System.out.println("Không tìm thấy khách hàng với ID = " + id);
            return;
        }

        System.out.println("Thông tin hiện tại: " + existing);
        String name = InputUtil.getString("Nhập tên mới (để trống nếu không thay đổi): ");
        String phone = InputUtil.getString("Nhập số điện thoại mới (để trống nếu không thay đổi): ");
        String email = InputUtil.getString("Nhập email mới (để trống nếu không thay đổi): ");
        String address = InputUtil.getString("Nhập địa chỉ mới (để trống nếu không thay đổi): ");

        if (!name.isEmpty()) existing.setName(name);
        if (!phone.isEmpty()) existing.setPhone(phone);
        if (!email.isEmpty()) existing.setEmail(email);
        if (!address.isEmpty()) existing.setAddress(address);

        customerService.updateCustomer(existing);
        System.out.println("Cập nhật thông tin khách hàng thành công.");
    }

    private void deleteCustomer() {
        int id = InputUtil.getInt("Nhập ID khách hàng cần xoá: ");
        Customer existing = customerService.getCustomerById(id);
        if (existing == null) {
            System.out.println("Không tìm thấy khách hàng với ID = " + id);
            return;
        }

        System.out.println("Thông tin khách hàng: " + existing);
        String confirm = InputUtil.getString("Bạn có chắc muốn xoá? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            customerService.deleteCustomer(id);
            System.out.println("Đã xoá khách hàng thành công.");
        } else {
            System.out.println("Huỷ thao tác xoá.");
        }
    }
}