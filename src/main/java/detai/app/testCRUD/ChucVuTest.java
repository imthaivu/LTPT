package detai.app.testCRUD;

import detai.app.dao.ChucVuDAO;
import detai.app.entity.ChucVu;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class ChucVuTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        ChucVuDAO dao = new ChucVuDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU ChucVu ===");
            System.out.println("1. Thêm ChucVu");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaCV");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    themChucVu(sc, dao);
                    break;
                case 2:
                    hienThiTatCa(dao);
                    break;
                case 3:
                    timChucVu(sc, dao);
                    break;
                case 4:
                    capNhatChucVu(sc, dao);
                    break;
                case 5:
                    xoaChucVu(sc, dao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
            }
        }
        System.out.println("Đã thoát chương trình test ChucVu.");
        sc.close();
        emf.close();
    }

    private static void themChucVu(Scanner sc, ChucVuDAO dao) {
        System.out.print("Nhập MaCV: ");
        String maCV = sc.nextLine().trim();
        System.out.print("Nhập Lương: ");
        double luong = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Nhập Tên CV: ");
        String tenCV = sc.nextLine().trim();

        ChucVu cv = new ChucVu(maCV, luong, tenCV);
        dao.save(cv);
        System.out.println("=> Đã thêm: " + cv);
    }

    private static void hienThiTatCa(ChucVuDAO dao) {
        List<ChucVu> list = dao.findAll();
        System.out.println("=== Danh sách ChucVu ===");
        for (ChucVu c : list) {
            System.out.println(c);
        }
    }

    private static void timChucVu(Scanner sc, ChucVuDAO dao) {
        System.out.print("Nhập MaCV: ");
        String maCV = sc.nextLine().trim();
        ChucVu cv = dao.findById(maCV);
        if (cv != null) {
            System.out.println("Đã tìm thấy: " + cv);
        } else {
            System.out.println("Không tìm thấy MaCV = " + maCV);
        }
    }

    private static void capNhatChucVu(Scanner sc, ChucVuDAO dao) {
        System.out.print("Nhập MaCV cần cập nhật: ");
        String maCV = sc.nextLine().trim();
        ChucVu cv = dao.findById(maCV);
        if (cv == null) {
            System.out.println("Không tìm thấy ChucVu!");
            return;
        }
        System.out.println("ChucVu hiện tại: " + cv);

        System.out.print("Nhập Lương mới: ");
        double luong = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Nhập TênCV mới: ");
        String tenCV = sc.nextLine().trim();

        cv.setLuong(luong);
        cv.setTenCV(tenCV);

        dao.update(cv);
        System.out.println("=> Đã update: " + cv);
    }

    private static void xoaChucVu(Scanner sc, ChucVuDAO dao) {
        System.out.print("Nhập MaCV cần xóa: ");
        String maCV = sc.nextLine().trim();
        dao.delete(maCV);
        System.out.println("=> Đã xóa nếu tồn tại MaCV = " + maCV);
    }
}