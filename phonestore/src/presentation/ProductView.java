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
            System.out.println("5. Tìm kiếm theo thương hiệu");
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
                    updateProductById();
                    break;
                case 4:
                    deleteProductById();
                    break;
                case 5:
                    searchProductByBrand();
                    break;
                case 6:
                    searchProductByPriceRange();
                    break;
                case 7:
                    searchProductByStock();
                    break;
                case 8:
                    System.out.println("Quay lại menu chính");
                    break;
                default:
                    System.out.println("chức năng không hợp lệ");
            }
        } while (choice != 8);
    }

    private static void displayAll() {
        List<Product> list = productService.getAllProducts();
        if (list.isEmpty()) {
            System.out.println("Không có sản phẩm nào!");
        } else {
            System.out.println("======================= Danh sách sản phẩm =========================");
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

    private static void updateProductById() {
        int id;
        Product existing;
        do {
            System.out.print("Nhập ID sản phẩm cần cập nhật: ");
            id = getValidInt("");
            existing = productService.getProductById(id);
            if (existing == null) {
                System.out.println("Không tìm thấy sản phẩm với ID này. Vui lòng thử lại.");
            }
        } while (existing == null);

        System.out.println("Thông tin hiện tại:");
        System.out.println(existing);

        System.out.println("Nhập thông tin mới:");
        System.out.print("Tên sản phẩm mới: ");
        String name = sc.nextLine();
        System.out.print("Hãng mới: ");
        String brand = sc.nextLine();
        double price = getValidDouble("Giá mới: ");
        int stock = getValidInt("Tồn kho mới: ");

        existing.setName(name);
        existing.setBrand(brand);
        existing.setPrice(price);
        existing.setStock(stock);

        if (productService.updateProduct(existing)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }

    private static void deleteProductById() {
        System.out.print("Nhập ID sản phẩm muốn xóa: ");
        int id = getValidInt("");
        Product existing = productService.getProductById(id);

        if (existing == null) {
            System.out.println("Không tìm thấy sản phẩm với ID: " + id);
            return;
        }
        System.out.println("Thông tin sản phẩm:");
        System.out.println(existing);

        System.out.print("Bạn có chắc chắn muốn xóa sản phẩm này không? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            boolean result = productService.deleteProduct(id);
            if (result) {
                System.out.println("Xóa sản phẩm thành công!");
            } else {
                System.out.println("Xóa sản phẩm thất bại!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }

    private static void searchProductByBrand() {
        System.out.print("Nhập từ khóa thương hiệu muốn tìm: ");
        String keyword = sc.nextLine().trim();

        List<Product> results = productService.searchProductByBrand(keyword);
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào với thương hiệu chứa: " + keyword);
        } else {
            System.out.println("Danh sách sản phẩm tìm thấy:");
            results.forEach(System.out::println);
        }
    }

    private static void searchProductByPriceRange() {
        try {
            System.out.print("Nhập giá tối thiểu: ");
            double min = Double.parseDouble(sc.nextLine());
            System.out.print("Nhập giá tối đa: ");
            double max = Double.parseDouble(sc.nextLine());
            if (min > max) {
                System.out.println("Giá tối thiểu phải nhỏ hơn hoặc bằng giá tối đa.");
                return;
            }
            List<Product> results = productService.searchProductByPriceRange(min, max);
            if (results.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào trong khoảng giá từ " + min + " đến " + max);
            } else {
                System.out.println("Danh sách sản phẩm tìm thấy:");
                results.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập đúng định dạng số!");
        }
    }

    private static void searchProductByStock() {
        try {
            System.out.print("Nhập số lượng tồn kho tối thiểu cần tìm: ");
            int minStock = Integer.parseInt(sc.nextLine());

            List<Product> results = productService.searchProductByStock(minStock);
            if (results.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào có tồn kho từ " + minStock + " trở lên.");
            } else {
                System.out.println("Danh sách sản phẩm có tồn kho từ " + minStock + ":");
                results.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số nguyên hợp lệ!");
        }
    }
}
