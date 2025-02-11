package detai.app.testCRUD;

import detai.app.dao.TaiKhoanDAO;
import detai.app.dao.NhanVienDAO;
import detai.app.entity.NhanVien;
import detai.app.entity.TaiKhoan;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class TaiKhoanTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        TaiKhoanDAO tkDAO = new TaiKhoanDAO(emf);
        NhanVienDAO nvDAO = new NhanVienDAO(emf);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("\n=== MENU TaiKhoan ===");
            System.out.println("1. Thêm TaiKhoan");
            System.out.println("2. Hiển thị tất cả");
            System.out.println("3. Tìm theo MaTK");
            System.out.println("4. Cập nhật");
            System.out.println("5. Xóa");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            switch(choice){
                case 1: themTK(sc, tkDAO, nvDAO); break;
                case 2: hienThiAll(tkDAO); break;
                case 3: timTK(sc, tkDAO); break;
                case 4: capNhatTK(sc, tkDAO, nvDAO); break;
                case 5: xoaTK(sc, tkDAO); break;
                case 6: running=false; break;
                default: System.out.println("Không hợp lệ!");
            }
        }
        System.out.println("Thoát TaiKhoanTest.");
        sc.close();
        emf.close();
    }

    private static void themTK(Scanner sc, TaiKhoanDAO tkDAO, NhanVienDAO nvDAO){
        System.out.print("MaTK: ");
        String maTK = sc.nextLine().trim();
        System.out.print("MatKhau: ");
        String mk = sc.nextLine().trim();
        System.out.print("TaiKhoan: ");
        String tk = sc.nextLine().trim();
        System.out.print("VaiTro: ");
        String vt = sc.nextLine().trim();

        System.out.print("Nhập MaNv để gán: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);

        TaiKhoan taiKhoan = new TaiKhoan(maTK, mk, tk, vt, nv);
        tkDAO.save(taiKhoan);
        System.out.println("=> Đã thêm: " + taiKhoan);
    }

    private static void hienThiAll(TaiKhoanDAO dao){
        List<TaiKhoan> list = dao.findAll();
        System.out.println("=== Danh sách TaiKhoan ===");
        for(TaiKhoan t : list){
            System.out.println(t);
        }
    }

    private static void timTK(Scanner sc, TaiKhoanDAO dao){
        System.out.print("Nhập MaTK: ");
        String ma = sc.nextLine().trim();
        TaiKhoan tk = dao.findById(ma);
        if(tk!=null){
            System.out.println("Tìm thấy: " + tk);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    private static void capNhatTK(Scanner sc, TaiKhoanDAO tkDAO, NhanVienDAO nvDAO){
        System.out.print("Nhập MaTK cần update: ");
        String maTK = sc.nextLine().trim();
        TaiKhoan tk = tkDAO.findById(maTK);
        if(tk==null){
            System.out.println("Không tìm thấy!");
            return;
        }
        System.out.println("Hiện tại: " + tk);

        System.out.print("MatKhau mới: ");
        String mk = sc.nextLine().trim();
        System.out.print("TaiKhoan mới: ");
        String user = sc.nextLine().trim();
        System.out.print("VaiTro mới: ");
        String vaitro = sc.nextLine().trim();
        System.out.print("MaNv mới: ");
        String maNv = sc.nextLine().trim();
        NhanVien nv = nvDAO.findById(maNv);

        tk.setMatKhau(mk);
        tk.setTaiKhoan(user);
        tk.setVaiTro(vaitro);
        tk.setNhanVien(nv);

        tkDAO.update(tk);
        System.out.println("=> Đã update: " + tk);
    }

    private static void xoaTK(Scanner sc, TaiKhoanDAO dao){
        System.out.print("Nhập MaTK cần xóa: ");
        String ma = sc.nextLine().trim();
        dao.delete(ma);
        System.out.println("=> Đã xóa (nếu tồn tại) MaTK = " + ma);
    }
}