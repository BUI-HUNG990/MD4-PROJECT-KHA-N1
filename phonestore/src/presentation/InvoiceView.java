package presentation;

import business.InvoiceService;
import business.impl.InvoiceServiceImpl;
import model.Invoice;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class InvoiceView {
    private static final Scanner sc = new Scanner(System.in);
    private static final InvoiceService invoiceService = new InvoiceServiceImpl();

    public static void displayInvoiceMenu() {
        int choice;
        do {
            System.out.println("\n========== QUẢN LÝ HÓA ĐƠN ==========");
            System.out.println("1. Hiển thị danh sách hóa đơn");
            System.out.println("2. Thêm mới hóa đơn");
            System.out.println("3. Tìm kiếm hóa đơn");
            System.out.println("4. Quay lại menu chính");
            System.out.println("======================================");
            System.out.print("Chọn chức năng: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    displaySearchInvoiceMenu();
                    break;
                case 4:
                    System.out.println("Quay lại menu chính");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }

        } while (choice != 4);
    }

    public static void displaySearchInvoiceMenu() {
        int choice;
        do {
            System.out.println("\n======= MENU TÌM KIẾM HÓA ĐƠN =======");
            System.out.println("1. Tìm theo tên khách hàng");
            System.out.println("2. Tìm theo ngày/tháng/năm");
            System.out.println("3. Quay lại menu hóa đơn");
            System.out.println("======================================");
            System.out.print("Chọn chức năng: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Quay lại menu hóa đơn");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }

        } while (choice != 3);
    }

}

