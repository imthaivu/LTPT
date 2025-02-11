package detai.app.testCRUD;

import detai.app.dao.CTHoaDonDAO;
import detai.app.dao.HoaDonDAO;
import detai.app.dao.SanPhamDAO;
import detai.app.entity.CTHoaDon;
import detai.app.entity.CTHoaDonId;
import detai.app.entity.HoaDon;
import detai.app.entity.SanPham;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class CTHoaDonTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        CTHoaDonDAO ctDAO = new CTHoaDonDAO(emf);
        HoaDonDAO hdDAO = new HoaDonDAO(emf);
        SanPhamDAO spDAO = new SanPhamDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running){
            System.out.println("\n=== MENU CT_HoaDon ===");
            System.out.println("1. Thêm CT_HoaDon");
            System.out.println("2. Hiển thị tất cả CT_HoaDon");
            System.out.println("3. Tìm CT_HoaDon (maSP, maHD, soLuong)");
            System.out.println("4. Cập nhật CT_HoaDon");
            System.out.println("5. Xóa CT_HoaDon");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch(choice){
                case 1: themCT(sc, ctDAO, hdDAO, spDAO); break;
                case 2: hienThiAll(ctDAO); break;
                case 3: timCT(sc, ctDAO); break;
                case 4: capNhatCT(sc, ctDAO, hdDAO, spDAO); break;
                case 5: xoaCT(sc, ctDAO); break;
                case 6: running=false; break;
                default: System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Thoát CTHoaDonTest.");
        sc.close();
        emf.close();
    }

    private static void themCT(Scanner sc, CTHoaDonDAO ctDAO, HoaDonDAO hdDAO, SanPhamDAO spDAO){
        System.out.print("Nhập maSP: ");
        String maSP = sc.nextLine().trim();
        System.out.print("Nhập maHD: ");
        String maHD = sc.nextLine().trim();
        System.out.print("Nhập SoLuong: ");
        int sl = Integer.parseInt(sc.nextLine().trim());

        SanPham sp = spDAO.findById(maSP);
        HoaDon hd = hdDAO.findById(maHD);

        CTHoaDon cthd = new CTHoaDon(maSP, maHD, sl, sp, hd);
        ctDAO.save(cthd);
        System.out.println("=> Đã thêm: " + cthd);
    }

    private static void hienThiAll(CTHoaDonDAO ctDAO){
        List<CTHoaDon> list = ctDAO.findAll();
        System.out.println("=== Danh sách CT_HoaDon ===");
        for(CTHoaDon c : list){
            System.out.println(c);
        }
    }

    private static void timCT(Scanner sc, CTHoaDonDAO ctDAO){
        System.out.print("Nhập maSP: ");
        String maSP = sc.nextLine().trim();
        System.out.print("Nhập maHD: ");
        String maHD = sc.nextLine().trim();
        System.out.print("Nhập SoLuong: ");
        int sl = Integer.parseInt(sc.nextLine().trim());

        CTHoaDonId id = new CTHoaDonId(maSP, maHD, sl);
        CTHoaDon cthd = ctDAO.findById(id);
        if(cthd!=null){
            System.out.println("Tìm thấy: " + cthd);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatCT(Scanner sc, CTHoaDonDAO ctDAO, HoaDonDAO hdDAO, SanPhamDAO spDAO){
        System.out.print("Nhập maSP cũ: ");
        String oldSP = sc.nextLine().trim();
        System.out.print("Nhập maHD cũ: ");
        String oldHD = sc.nextLine().trim();
        System.out.print("Nhập soLuong cũ: ");
        int oldSL = Integer.parseInt(sc.nextLine().trim());

        CTHoaDonId oldId = new CTHoaDonId(oldSP, oldHD, oldSL);
        CTHoaDon cthd = ctDAO.findById(oldId);
        if(cthd==null){
            System.out.println("Không tìm thấy CT_HoaDon cũ!");
            return;
        }
        System.out.println("Hiện tại: " + cthd);

        System.out.println("Nhập THÔNG TIN MỚI (maSP, maHD, soLuong): ");
        System.out.print("maSP mới: ");
        String newSP = sc.nextLine().trim();
        System.out.print("maHD mới: ");
        String newHD = sc.nextLine().trim();
        System.out.print("soLuong mới: ");
        int newSL = Integer.parseInt(sc.nextLine().trim());

        // Tìm entity SP & HD mới
        SanPham sp = spDAO.findById(newSP);
        HoaDon hd = hdDAO.findById(newHD);

        // Update PK & mối quan hệ
        cthd.setMaSP(newSP);
        cthd.setMaHD(newHD);
        cthd.setSoLuong(newSL);
        cthd.setSanPham(sp);
        cthd.setHoaDon(hd);

        // Ghi DB
        ctDAO.update(cthd);
        System.out.println("=> Đã update CT_HoaDon: " + cthd);
    }

    private static void xoaCT(Scanner sc, CTHoaDonDAO ctDAO){
        System.out.print("Nhập maSP: ");
        String maSP = sc.nextLine().trim();
        System.out.print("Nhập maHD: ");
        String maHD = sc.nextLine().trim();
        System.out.print("Nhập soLuong: ");
        int soLuong = Integer.parseInt(sc.nextLine().trim());

        CTHoaDonId id = new CTHoaDonId(maSP, maHD, soLuong);
        ctDAO.delete(id);
        System.out.println("=> Đã xóa (nếu tồn tại) " + id);
    }
}