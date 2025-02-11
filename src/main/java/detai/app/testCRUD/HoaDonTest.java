package detai.app.testCRUD;

import detai.app.dao.HoaDonDAO;
import detai.app.dao.KhachHangDAO;
import detai.app.dao.NhanVienDAO;
import detai.app.entity.HoaDon;
import detai.app.entity.KhachHang;
import detai.app.entity.NhanVien;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class HoaDonTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        HoaDonDAO hdDAO = new HoaDonDAO(emf);
        KhachHangDAO khDAO = new KhachHangDAO(emf);
        NhanVienDAO nvDAO = new NhanVienDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("\n=== MENU HoaDon ===");
            System.out.println("1. Thêm HoaDon");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaHD");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch(choice){
                case 1: themHD(sc, hdDAO, khDAO, nvDAO); break;
                case 2: hienThiAll(hdDAO); break;
                case 3: timHD(sc, hdDAO); break;
                case 4: capNhatHD(sc, hdDAO, khDAO, nvDAO); break;
                case 5: xoaHD(sc, hdDAO); break;
                case 6: running=false; break;
                default: System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Thoát HoaDonTest.");
        sc.close();
        emf.close();
    }

    private static void themHD(Scanner sc, HoaDonDAO hdDAO, KhachHangDAO khDAO, NhanVienDAO nvDAO){
        System.out.print("MaHD: ");
        String maHD = sc.nextLine().trim();
        System.out.println("NgayBan tạm set = now()");
        LocalDateTime ngayBan = LocalDateTime.now();

        System.out.print("Nhập MaKH: ");
        String maKH = sc.nextLine().trim();
        KhachHang kh = khDAO.findById(maKH);

        System.out.print("Nhập MaNv: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);

        HoaDon hd = new HoaDon(maHD, ngayBan, kh, nv);
        hdDAO.save(hd);
        System.out.println("=> Đã thêm: " + hd);
    }

    private static void hienThiAll(HoaDonDAO hdDAO){
        List<HoaDon> list = hdDAO.findAll();
        System.out.println("=== Danh sách HoaDon ===");
        for(HoaDon hd: list){
            System.out.println(hd);
        }
    }

    private static void timHD(Scanner sc, HoaDonDAO hdDAO){
        System.out.print("Nhập MaHD: ");
        String maHD = sc.nextLine().trim();
        HoaDon hd = hdDAO.findById(maHD);
        if(hd!=null) {
            System.out.println("Tìm thấy: " + hd);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatHD(Scanner sc, HoaDonDAO hdDAO, KhachHangDAO khDAO, NhanVienDAO nvDAO){
        System.out.print("Nhập MaHD cần update: ");
        String maHD = sc.nextLine().trim();
        HoaDon hd = hdDAO.findById(maHD);
        if(hd==null){
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + hd);

        System.out.println("NgayBan mới (tạm set now)");
        LocalDateTime nb = LocalDateTime.now();

        System.out.print("MaKH mới: ");
        String maKH = sc.nextLine().trim();
        KhachHang kh = khDAO.findById(maKH);

        System.out.print("MaNv mới: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);

        hd.setNgayBan(nb);
        hd.setKhachHang(kh);
        hd.setNhanVien(nv);

        hdDAO.update(hd);
        System.out.println("=> Đã update: " + hd);
    }

    private static void xoaHD(Scanner sc, HoaDonDAO hdDAO){
        System.out.print("Nhập MaHD cần xóa: ");
        String maHD = sc.nextLine().trim();
        hdDAO.delete(maHD);
        System.out.println("=> Đã xóa (nếu tồn tại) MaHD=" + maHD);
    }
}