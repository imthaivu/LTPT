package detai.app.testCRUD;

import detai.app.dao.NhanVienDAO;
import detai.app.dao.ChucVuDAO;
import detai.app.entity.ChucVu;
import detai.app.entity.NhanVien;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class NhanVienTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        NhanVienDAO nvDAO = new NhanVienDAO(emf);
        ChucVuDAO cvDAO = new ChucVuDAO(emf); // Để map NhanVien->ChucVu

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU NhanVien ===");
            System.out.println("1. Thêm NhanVien");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaNv");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    themNhanVien(sc, nvDAO, cvDAO);
                    break;
                case 2:
                    hienThiAll(nvDAO);
                    break;
                case 3:
                    timNhanVien(sc, nvDAO);
                    break;
                case 4:
                    capNhatNhanVien(sc, nvDAO, cvDAO);
                    break;
                case 5:
                    xoaNhanVien(sc, nvDAO);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ!");
            }
        }
        System.out.println("Đã thoát chương trình test NhanVien.");
        sc.close();
        emf.close();
    }

    private static void themNhanVien(Scanner sc, NhanVienDAO nvDAO, ChucVuDAO cvDAO) {
        System.out.print("MaNv: ");
        String maNv = sc.nextLine().trim();
        System.out.print("CaLamViec: ");
        String caLam = sc.nextLine().trim();
        System.out.print("GioiTinh (true/false): ");
        boolean gioiTinh = Boolean.parseBoolean(sc.nextLine().trim());
        System.out.print("HoTen: ");
        String hoTen = sc.nextLine().trim();
        System.out.print("Luong: ");
        double luong = Double.parseDouble(sc.nextLine().trim());
        System.out.println("NgaySinh (tạm đặt Now - 25 năm)");
        LocalDateTime ngaySinh = LocalDateTime.now().minusYears(25);
        System.out.print("SoDT: ");
        String soDT = sc.nextLine().trim();
        System.out.print("TrangThai: ");
        String tt = sc.nextLine().trim();

        System.out.print("Nhập MaCV để map: ");
        String maCV = sc.nextLine().trim();
        ChucVu cv = cvDAO.findById(maCV);

        NhanVien nv = new NhanVien(maNv, caLam, gioiTinh, hoTen, luong, ngaySinh, soDT, tt, cv);
        nvDAO.save(nv);
        System.out.println("=> Đã thêm: " + nv);
    }

    private static void hienThiAll(NhanVienDAO nvDAO) {
        List<NhanVien> list = nvDAO.findAll();
        System.out.println("=== Danh sách NhanVien ===");
        for (NhanVien nv : list) {
            System.out.println(nv);
        }
    }

    private static void timNhanVien(Scanner sc, NhanVienDAO nvDAO) {
        System.out.print("Nhập MaNv: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);
        if (nv != null) {
            System.out.println("Đã tìm thấy: " + nv);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatNhanVien(Scanner sc, NhanVienDAO nvDAO, ChucVuDAO cvDAO) {
        System.out.print("Nhập MaNv cần update: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);
        if (nv == null) {
            System.out.println("Không tìm thấy NV này.");
            return;
        }
        System.out.println("Hiện tại: " + nv);

        System.out.print("CaLamViec mới: ");
        String caLam = sc.nextLine().trim();
        System.out.print("GioiTinh (true/false): ");
        boolean gt = Boolean.parseBoolean(sc.nextLine().trim());
        System.out.print("HoTen mới: ");
        String hoTen = sc.nextLine().trim();
        System.out.print("Luong: ");
        double luong = Double.parseDouble(sc.nextLine().trim());
        System.out.print("SoDT: ");
        String soDT = sc.nextLine().trim();
        System.out.print("TrangThai: ");
        String tt = sc.nextLine().trim();
        System.out.print("Nhập MaCV mới: ");
        String maCV = sc.nextLine().trim();
        ChucVu cv = cvDAO.findById(maCV);

        nv.setCaLamViec(caLam);
        nv.setGioiTinh(gt);
        nv.setHoTen(hoTen);
        nv.setLuong(luong);
        nv.setSoDT(soDT);
        nv.setTrangThai(tt);
        nv.setChucVu(cv);

        nvDAO.update(nv);
        System.out.println("=> Đã update: " + nv);
    }

    private static void xoaNhanVien(Scanner sc, NhanVienDAO nvDAO) {
        System.out.print("Nhập MaNv: ");
        String maNv = sc.nextLine().trim();
        nvDAO.delete(maNv);
        System.out.println("=> Đã xóa (nếu tồn tại): " + maNv);
    }
}