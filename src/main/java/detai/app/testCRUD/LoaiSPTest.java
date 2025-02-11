package detai.app.testCRUD;

import detai.app.dao.LoaiSPDAO;
import detai.app.entity.LoaiSP;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class LoaiSPTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        LoaiSPDAO dao = new LoaiSPDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("\n=== MENU LoaiSP ===");
            System.out.println("1. Thêm LoaiSP");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaLoaiSP");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch(choice){
                case 1: themLoai(sc, dao); break;
                case 2: hienThiAll(dao); break;
                case 3: timLoai(sc, dao); break;
                case 4: capNhatLoai(sc, dao); break;
                case 5: xoaLoai(sc, dao); break;
                case 6: running=false; break;
                default: System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Đã thoát LoaiSPTest.");
        sc.close();
        emf.close();
    }

    private static void themLoai(Scanner sc, LoaiSPDAO dao){
        System.out.print("MaLoaiSP: ");
        String ma = sc.nextLine().trim();
        System.out.print("TenLoaiSP: ");
        String ten = sc.nextLine().trim();

        LoaiSP lsp = new LoaiSP(ma, ten);
        dao.save(lsp);
        System.out.println("=> Đã thêm: " + lsp);
    }

    private static void hienThiAll(LoaiSPDAO dao){
        List<LoaiSP> list = dao.findAll();
        System.out.println("=== Danh sách LoaiSP ===");
        for (LoaiSP l : list){
            System.out.println(l);
        }
    }

    private static void timLoai(Scanner sc, LoaiSPDAO dao){
        System.out.print("Nhập MaLoaiSP: ");
        String ma = sc.nextLine().trim();
        LoaiSP l = dao.findById(ma);
        if (l!=null){
            System.out.println("Tìm thấy: " + l);
        }else{
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatLoai(Scanner sc, LoaiSPDAO dao){
        System.out.print("MaLoaiSP cần update: ");
        String ma = sc.nextLine().trim();
        LoaiSP lsp = dao.findById(ma);
        if (lsp==null){
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + lsp);
        System.out.print("TenLoaiSP mới: ");
        String ten = sc.nextLine().trim();

        lsp.setTenLoaiSP(ten);
        dao.update(lsp);
        System.out.println("=> Đã update: " + lsp);
    }

    private static void xoaLoai(Scanner sc, LoaiSPDAO dao){
        System.out.print("Nhập MaLoaiSP cần xóa: ");
        String ma = sc.nextLine().trim();
        dao.delete(ma);
        System.out.println("=> Đã xóa (nếu tồn tại) MaLoaiSP=" + ma);
    }
}