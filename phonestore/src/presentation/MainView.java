package presentation;

import java.util.Scanner;
import presentation.CustomerView;
import presentation.ProductView;

public class MainView {
    private static final Scanner sc = new Scanner(System.in);
    private static final CustomerView customerView = new CustomerView();

    public void startMenu() {
        int choice;
        do {
            System.out.println("========== HỆ THỐNG QUẢN LÝ CỬA HÀNG ==========");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Thoát");
            System.out.println("==============================================");
            System.out.print("Nhập lựa chọn: ");
            choice = getValidChoice(1, 2);
            switch (choice) {
                case 1:
                    loginAdmin();
                    break;
                case 2:
                    System.out.println("Đã thoát chương trình.");
                    break;
            }
        } while (choice != 2);
    }

    private void loginAdmin() {
        System.out.println("========== ĐĂNG NHẬP QUẢN TRỊ ==========");
        System.out.print("Tài khoản: ");
        String username = sc.nextLine();
        System.out.print("Mật khẩu : ");
        String password = sc.nextLine();
        System.out.print("========================================\n");

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Đăng nhập thành công!");
            showMainMenu();
        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu! Vui lòng thử lại.\n");
        }
    }

    private void showMainMenu() {
        int choice;
        do {
            System.out.println("========== MENU CHÍNH ==========");
            System.out.println("1. Quản lý sản phẩm điện thoại");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý hoá đơn");
            System.out.println("4. Thống kê doanh thu");
            System.out.println("5. Đăng xuất");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn: ");
            choice = getValidChoice(1, 5);
            switch (choice) {
                case 1:
                    ProductView.showMenu();
                    break;
                case 2:
                    customerView.displayCustomerMenu();
                    break;
                case 3:
                    InvoiceView.displayInvoiceMenu();
                    break;
                case 4:
                    RevenueView.displayRevenueMenu();
                    break;
                case 5:
                    System.out.println("Đã đăng xuất!");
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
            }
        } while (choice != 5);
    }

    private int getValidChoice(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(sc.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Lựa chọn không hợp lệ! Nhập lại: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Phải nhập số! Nhập lại: ");
            }
        }
    }
}