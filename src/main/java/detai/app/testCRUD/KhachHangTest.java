package detai.app.testCRUD;

import detai.app.dao.KhachHangDAO;
import detai.app.entity.KhachHang;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class KhachHangTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        KhachHangDAO dao = new KhachHangDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU KhachHang ===");
            System.out.println("1. Thêm KhachHang");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaKH");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    themKhachHang(sc, dao);
                    break;
                case 2:
                    hienThiAll(dao);
                    break;
                case 3:
                    timKhachHang(sc, dao);
                    break;
                case 4:
                    capNhatKhachHang(sc, dao);
                    break;
                case 5:
                    xoaKhachHang(sc, dao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Đã thoát KhachHangTest.");
        sc.close();
        emf.close();
    }

    private static void themKhachHang(Scanner sc, KhachHangDAO dao) {
        System.out.print("MaKH: ");
        String maKH = sc.nextLine().trim();
        System.out.print("DiaChi: ");
        String diaChi = sc.nextLine().trim();
        System.out.print("SDT: ");
        String sdt = sc.nextLine().trim();
        System.out.print("TenKH: ");
        String tenKH = sc.nextLine().trim();

        KhachHang kh = new KhachHang(maKH, diaChi, sdt, tenKH);
        dao.save(kh);
        System.out.println("=> Đã thêm: " + kh);
    }

    private static void hienThiAll(KhachHangDAO dao) {
        List<KhachHang> list = dao.findAll();
        System.out.println("=== Danh sách KhachHang ===");
        for (KhachHang kh : list) {
            System.out.println(kh);
        }
    }

    private static void timKhachHang(Scanner sc, KhachHangDAO dao) {
        System.out.print("Nhập MaKH: ");
        String ma = sc.nextLine().trim();
        KhachHang kh = dao.findById(ma);
        if (kh != null) {
            System.out.println("Tìm thấy: " + kh);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatKhachHang(Scanner sc, KhachHangDAO dao) {
        System.out.print("Nhập MaKH cần update: ");
        String ma = sc.nextLine().trim();
        KhachHang kh = dao.findById(ma);
        if (kh == null) {
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + kh);

        System.out.print("DiaChi mới: ");
        String dc = sc.nextLine().trim();
        System.out.print("SDT mới: ");
        String sdt = sc.nextLine().trim();
        System.out.print("TenKH mới: ");
        String ten = sc.nextLine().trim();

        kh.setDiaChi(dc);
        kh.setSdt(sdt);
        kh.setTenKH(ten);

        dao.update(kh);
        System.out.println("=> Đã update: " + kh);
    }

    private static void xoaKhachHang(Scanner sc, KhachHangDAO dao) {
        System.out.print("Nhập MaKH cần xóa: ");
        String ma = sc.nextLine().trim();
        dao.delete(ma);
        System.out.println("=> Đã xóa (nếu tồn tại) MaKH = " + ma);
    }
}