package presentation;

import business.InvoiceService;
import business.impl.InvoiceServiceImpl;
import model.Invoice;
import utils.InputUtil;
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
                    showAllInvoices();
                    break;
                case 2:
                    addInvoice();
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
                    searchInvoiceByCustomerName();
                    break;
                case 2:
                    searchInvoiceByDate();
                    break;
                case 3:
                    System.out.println("Quay lại menu hóa đơn");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }

        } while (choice != 3);
    }
    private static void addInvoice() {
        System.out.println("\n--- THÊM MỚI HÓA ĐƠN ---");

        int customerId = InputUtil.getInt("Nhập ID khách hàng: ");
        int productId = InputUtil.getInt("Nhập ID sản phẩm: ");
        int quantity = InputUtil.getInt("Nhập số lượng: ");

        invoiceService.placeOrder(customerId, productId, quantity);
    }
    private static void showAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào.");
        } else {
            System.out.println("\n=============== DANH SÁCH HÓA ĐƠN ===============");
            System.out.printf("%-8s | %-8s | %-20s | %-12s\n",
                    "Mã HD", "Mã KH", "Ngày tạo", "Tổng tiền");
            System.out.println("-----------------------------------------------------------");
            for (Invoice invoice : invoices) {
                System.out.println(invoice);
            }
        }
    }
    private static void searchInvoiceByCustomerName() {
        String keyword;
        do {
            System.out.print("Nhập tên khách hàng cần tìm: ");
            keyword = sc.nextLine().trim();
            if (keyword.isEmpty()) {
                System.out.println("Tên không được để trống. Vui lòng nhập lại!");
            }
        } while (keyword.isEmpty());

        List<Invoice> result = invoiceService.searchInvoicesByCustomerName(keyword);
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn nào phù hợp với tên \"" + keyword + "\".");
        } else {
            System.out.println("\n=============== KẾT QUẢ TÌM KIẾM ===============");
            System.out.printf("%-8s | %-8s | %-20s | %-12s\n",
                    "Mã HD", "Mã KH", "Ngày tạo", "Tổng tiền");
            System.out.println("-----------------------------------------------------------");
            for (Invoice invoice : result) {
                System.out.println(invoice);
            }
        }
    }
    private static void searchInvoiceByDate() {
        java.sql.Date searchDate = null;
        do {
            System.out.print("Nhập ngày cần tìm (định dạng yyyy-MM-dd): ");
            String input = sc.nextLine().trim();

            try {
                searchDate = java.sql.Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Ngày không hợp lệ. Vui lòng nhập đúng định dạng yyyy-MM-dd.");
            }
        } while (searchDate == null);

        List<Invoice> result = invoiceService.searchInvoicesByDate(searchDate);
        if (result.isEmpty()) {
            System.out.println("Không có hóa đơn nào trong ngày " + searchDate + ".");
        } else {
            System.out.println("\n=============== KẾT QUẢ TÌM KIẾM ===============");
            System.out.printf("%-8s | %-8s | %-20s | %-12s\n",
                    "Mã HD", "Mã KH", "Ngày tạo", "Tổng tiền");
            System.out.println("-----------------------------------------------------------");
            for (Invoice invoice : result) {
                System.out.println(invoice);
            }
        }
    }


}




