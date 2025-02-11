package detai.app.testCRUD;

import detai.app.dao.NhaCungCapDAO;
import detai.app.entity.NhaCungCap;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class NhaCungCapTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        NhaCungCapDAO dao = new NhaCungCapDAO(emf);

        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU NhaCungCap ===");
            System.out.println("1. Thêm NhaCungCap");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaNCC");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    themNCC(sc, dao);
                    break;
                case 2:
                    hienThiAll(dao);
                    break;
                case 3:
                    timNCC(sc, dao);
                    break;
                case 4:
                    capNhatNCC(sc, dao);
                    break;
                case 5:
                    xoaNCC(sc, dao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Đã thoát NhaCungCapTest.");
        sc.close();
        emf.close();
    }

    private static void themNCC(Scanner sc, NhaCungCapDAO dao) {
        System.out.print("MaNCC: ");
        String ma = sc.nextLine().trim();
        System.out.print("DiaChi: ");
        String dc = sc.nextLine().trim();
        System.out.print("SoDT: ");
        String sdt = sc.nextLine().trim();
        System.out.print("TenNCC: ");
        String ten = sc.nextLine().trim();

        NhaCungCap ncc = new NhaCungCap(ma, dc, sdt, ten);
        dao.save(ncc);
        System.out.println("=> Đã thêm: " + ncc);
    }

    private static void hienThiAll(NhaCungCapDAO dao) {
        List<NhaCungCap> list = dao.findAll();
        System.out.println("=== Danh sách NhaCungCap ===");
        for (NhaCungCap n : list) {
            System.out.println(n);
        }
    }

    private static void timNCC(Scanner sc, NhaCungCapDAO dao) {
        System.out.print("Nhập MaNCC: ");
        String ma = sc.nextLine().trim();
        NhaCungCap ncc = dao.findById(ma);
        if (ncc != null) {
            System.out.println("Tìm thấy: " + ncc);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatNCC(Scanner sc, NhaCungCapDAO dao) {
        System.out.print("MaNCC cần update: ");
        String ma = sc.nextLine().trim();
        NhaCungCap ncc = dao.findById(ma);
        if (ncc == null) {
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + ncc);

        System.out.print("DiaChi mới: ");
        String dc = sc.nextLine().trim();
        System.out.print("SoDT mới: ");
        String sdt = sc.nextLine().trim();
        System.out.print("TenNCC mới: ");
        String ten = sc.nextLine().trim();

        ncc.setDiaChi(dc);
        ncc.setSoDT(sdt);
        ncc.setTenNCC(ten);

        dao.update(ncc);
        System.out.println("=> Đã update: " + ncc);
    }

    private static void xoaNCC(Scanner sc, NhaCungCapDAO dao) {
        System.out.print("Nhập MaNCC cần xóa: ");
        String ma = sc.nextLine().trim();
        dao.delete(ma);
        System.out.println("=> Đã xóa (nếu tồn tại) MaNCC=" + ma);
    }
}