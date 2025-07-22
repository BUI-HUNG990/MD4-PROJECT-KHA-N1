package presentation;

import business.IProductService;
import business.impl.ProductServiceImpl;
import model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductView {
    private static final Scanner sc = new Scanner(System.in);
    private static final IProductService productService = new ProductServiceImpl();
    public static void showMenu() {
        int choice;
        do {
            System.out.println("========== QUẢN LÝ SẢN PHẨM ==========");
            System.out.println("1. Hiển thị danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm mới");
            System.out.println("3. Cập nhật thông tin sản phẩm");
            System.out.println("4. Xóa sản phẩm theo Id");
            System.out.println("5. Tìm kiếm theo Brand");
            System.out.println("6. Tìm kiếm theo khoảng giá");
            System.out.println("7. Tìm kiếm theo tồn kho");
            System.out.println("8. Quay lại menu chính");
            System.out.println("======================================");
            System.out.print("Lựa chọn của bạn: ");
            choice = getValidChoice(1, 8);
            switch (choice) {
                case 1:
                    displayAll();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    addNewProduct();
                    break;
                case 4:
                    addNewProduct();
                    break;
                case 5:
                    addNewProduct();
                    break;
                case 6:
                    addNewProduct();
                    break;
                case 7:
                    addNewProduct();
                    break;
                case 8:
                    System.out.println("Quay lại menu chính...");
                    break;
                default:
                    System.out.println();
            }
        } while (choice != 8);
    }

    private static void displayAll() {
        List<Product> list = productService.getAllProducts();
        if (list.isEmpty()) {
            System.out.println("Không có sản phẩm nào!");
        } else {
            System.out.println("Danh sách sản phẩm:");
            list.forEach(System.out::println);
        }
    }

    private static void addNewProduct() {
        System.out.println("Nhập thông tin sản phẩm mới:");
        System.out.print("Tên sản phẩm: ");
        String name = sc.nextLine();
        System.out.print("Hãng sản xuất: ");
        String brand = sc.nextLine();
        double price = getValidDouble("Giá: ");
        int stock = getValidInt("Tồn kho: ");

        Product product = new Product(0, name, brand, price, stock);
        if (productService.addProduct(product)) {
            System.out.println("Thêm sản phẩm thành công!");
        } else {
            System.out.println("Thêm sản phẩm thất bại!");
        }
    }

    private static int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice >= min && choice <= max) return choice;
                System.out.print("Lựa chọn không hợp lệ! Nhập lại: ");
            } catch (NumberFormatException e) {
                System.out.print("Phải nhập số! Nhập lại: ");
            }
        }
    }

    private static double getValidDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Phải nhập số thực!");
            }
        }
    }

    private static int getValidInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Phải nhập số nguyên!");
            }
        }
    }
}

