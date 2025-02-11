package detai.app.testCRUD;

import detai.app.dao.SanPhamDAO;
import detai.app.dao.LoaiSPDAO;
import detai.app.dao.NhaCungCapDAO;
import detai.app.entity.LoaiSP;
import detai.app.entity.NhaCungCap;
import detai.app.entity.SanPham;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class SanPhamTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        SanPhamDAO spDAO = new SanPhamDAO(emf);
        LoaiSPDAO lspDAO = new LoaiSPDAO(emf);
        NhaCungCapDAO nccDAO = new NhaCungCapDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("\n=== MENU SanPham ===");
            System.out.println("1. Thêm SanPham");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaSP");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch(choice){
                case 1: themSanPham(sc, spDAO, lspDAO, nccDAO); break;
                case 2: hienThiAll(spDAO); break;
                case 3: timSP(sc, spDAO); break;
                case 4: capNhatSP(sc, spDAO, lspDAO, nccDAO); break;
                case 5: xoaSP(sc, spDAO); break;
                case 6: running=false; break;
                default: System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Thoát SanPhamTest.");
        sc.close();
        emf.close();
    }

    private static void themSanPham(Scanner sc, SanPhamDAO spDAO, LoaiSPDAO lspDAO, NhaCungCapDAO nccDAO){
        System.out.print("MaSP: ");
        String ma = sc.nextLine().trim();
        System.out.print("Anh (path): ");
        String anh = sc.nextLine().trim();
        System.out.print("DonVi: ");
        String donVi = sc.nextLine().trim();
        System.out.print("Gia: ");
        double gia = Double.parseDouble(sc.nextLine().trim());
        System.out.print("SoLuong: ");
        int soLuong = Integer.parseInt(sc.nextLine().trim());
        System.out.print("TenSP: ");
        String tenSP = sc.nextLine().trim();

        System.out.print("Nhập MaLoaiSP: ");
        String maLoai = sc.nextLine().trim();
        LoaiSP lsp = lspDAO.findById(maLoai);

        System.out.print("Nhập MaNCC: ");
        String maNCC = sc.nextLine().trim();
        NhaCungCap ncc = nccDAO.findById(maNCC);

        SanPham sp = new SanPham(ma, anh, donVi, gia, soLuong, tenSP, lsp, ncc);
        spDAO.save(sp);
        System.out.println("=> Đã thêm: " + sp);
    }

    private static void hienThiAll(SanPhamDAO dao){
        List<SanPham> list = dao.findAll();
        System.out.println("=== Danh sách SanPham ===");
        for(SanPham sp : list){
            System.out.println(sp);
        }
    }

    private static void timSP(Scanner sc, SanPhamDAO dao){
        System.out.print("Nhập MaSP: ");
        String ma = sc.nextLine().trim();
        SanPham sp = dao.findById(ma);
        if(sp!=null) {
            System.out.println("Tìm thấy: " + sp);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatSP(Scanner sc, SanPhamDAO spDAO, LoaiSPDAO lspDAO, NhaCungCapDAO nccDAO){
        System.out.print("Nhập MaSP cần update: ");
        String maSP = sc.nextLine().trim();
        SanPham sp = spDAO.findById(maSP);
        if(sp==null){
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + sp);

        System.out.print("Anh (path) mới: ");
        String anh = sc.nextLine().trim();
        System.out.print("DonVi mới: ");
        String donVi = sc.nextLine().trim();
        System.out.print("Gia: ");
        double gia = Double.parseDouble(sc.nextLine().trim());
        System.out.print("SoLuong: ");
        int soLuong = Integer.parseInt(sc.nextLine().trim());
        System.out.print("TenSP: ");
        String tenSP = sc.nextLine().trim();

        System.out.print("MaLoaiSP mới: ");
        String maLoai = sc.nextLine().trim();
        LoaiSP lsp = lspDAO.findById(maLoai);

        System.out.print("MaNCC mới: ");
        String maNCC = sc.nextLine().trim();
        NhaCungCap ncc = nccDAO.findById(maNCC);

        sp.setAnh(anh);
        sp.setDonVi(donVi);
        sp.setGia(gia);
        sp.setSoLuong(soLuong);
        sp.setTenSP(tenSP);
        sp.setLoaiSP(lsp);
        sp.setNhaCungCap(ncc);

        spDAO.update(sp);
        System.out.println("=> Đã update: " + sp);
    }

    private static void xoaSP(Scanner sc, SanPhamDAO dao){
        System.out.print("Nhập MaSP cần xóa: ");
        String ma = sc.nextLine().trim();
        dao.delete(ma);
        System.out.println("=> Đã xóa (nếu tồn tại): " + ma);
    }
}