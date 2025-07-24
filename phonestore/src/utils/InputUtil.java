package utils;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);

    public static int getInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    public static double getDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số thực hợp lệ.");
            }
        }
    }

    public static String getString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    public static boolean confirm(String msg) {
        while (true) {
            System.out.print(msg + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Chỉ được nhập 'y' hoặc 'n'.");
        }
    }

}
