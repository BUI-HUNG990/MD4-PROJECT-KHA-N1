package presentation;

import business.InvoiceService;
import business.impl.InvoiceServiceImpl;

import java.util.Map;
import java.util.Scanner;

public class RevenueView {
    private static final InvoiceService invoiceService = new InvoiceServiceImpl();
    private static final Scanner sc = new Scanner(System.in);

    public static void displayRevenueMenu() {
        int choice;
        do {
            System.out.println("\n======= THỐNG KÊ DOANH THU =======");
            System.out.println("1. Doanh thu theo ngày");
            System.out.println("2. Doanh thu theo tháng");
            System.out.println("3. Doanh thu theo năm");
            System.out.println("4. Quay lại menu chính");
            System.out.println("==================================");
            System.out.print("Chọn chức năng: ");

            while (!sc.hasNextInt()) {
                System.out.print("Vui lòng nhập số hợp lệ: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // bỏ qua dấu xuống dòng

            switch (choice) {
                case 1:
                    displayRevenueBy("DATE");
                    break;
                case 2:
                    displayRevenueBy("MONTH");
                    break;
                case 3:
                    displayRevenueBy("YEAR");
                    break;
                case 4:
                    System.out.println("⬅Quay lại menu chính");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (choice != 4);
    }

    private static void displayRevenueBy(String type) {
        Map<String, Double> revenues = switch (type) {
            case "DATE" -> invoiceService.getRevenueByDate();
            case "MONTH" -> invoiceService.getRevenueByMonth();
            case "YEAR" -> invoiceService.getRevenueByYear();
            default -> null;
        };

        if (revenues == null || revenues.isEmpty()) {
            System.out.println("Không có dữ liệu doanh thu.");
            return;
        }

        System.out.println("\n------ DOANH THU THEO " + type + " ------");
        System.out.printf("%-15s | %-15s\n", type.equals("DATE") ? "Ngày" : type.equals("MONTH") ? "Tháng" : "Năm", "Tổng doanh thu");
        System.out.println("-------------------------------");
        for (Map.Entry<String, Double> entry : revenues.entrySet()) {
            System.out.printf("%-15s | %,.2f VND\n", entry.getKey(), entry.getValue());
        }
    }
}

