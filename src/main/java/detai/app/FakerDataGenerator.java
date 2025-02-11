package detai.app;

import detai.app.dao.*;
import detai.app.entity.*;
import net.datafaker.Faker;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakerDataGenerator {
    public static void main(String[] args) {
        // 1. Tạo EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");

        // 2. Tạo DAO cho tất cả bảng
        ChucVuDAO chucVuDAO = new ChucVuDAO(emf);
        NhanVienDAO nhanVienDAO = new NhanVienDAO(emf);
        KhachHangDAO khachHangDAO = new KhachHangDAO(emf);
        NhaCungCapDAO nccDAO = new NhaCungCapDAO(emf);
        LoaiSPDAO loaiSPDAO = new LoaiSPDAO(emf);
        SanPhamDAO spDAO = new SanPhamDAO(emf);
        HoaDonDAO hdDAO = new HoaDonDAO(emf);
        CTHoaDonDAO cthdDAO = new CTHoaDonDAO(emf);
        TaiKhoanDAO tkDAO = new TaiKhoanDAO(emf);

        // 3. Tạo Faker
        Faker faker = new Faker();
        Random rand = new Random();

        System.out.println("=== Bắt đầu sinh dữ liệu giả & thêm vào DB ===");

        // -----------------------------------------------------
        // A) Tạo ChucVu (3 bản ghi)
        List<ChucVu> listChucVu = new ArrayList<>();
        for(int i = 1; i <= 3; i++){
            ChucVu cv = new ChucVu(
                    "FAKE_CV" + i,
                    (double) faker.number().numberBetween(5_000_000, 30_000_000),
                    faker.job().title()
            );
            chucVuDAO.save(cv);
            listChucVu.add(cv);
            System.out.println("Đã insert ChucVu: " + cv);
        }

        // -----------------------------------------------------
        // B) Tạo NhanVien (5 bản ghi, tham chiếu random ChucVu)
        List<NhanVien> listNV = new ArrayList<>();
        for(int i=1; i<=5; i++){
            ChucVu randomCV = listChucVu.get(rand.nextInt(listChucVu.size()));

            NhanVien nv = new NhanVien(
                    "FAKE_NV" + i,
                    "Ca " + faker.number().numberBetween(1,3),      // Ca 1,2
                    faker.bool().bool(),                           // true/false
                    faker.name().fullName(),
                    (double) faker.number().numberBetween(7_000_000, 15_000_000),
                    LocalDateTime.now().minusYears(faker.number().numberBetween(20, 40)),
                    faker.phoneNumber().phoneNumber(),
                    "Đang làm",
                    randomCV
            );
            nhanVienDAO.save(nv);
            listNV.add(nv);
            System.out.println("Đã insert NhanVien: " + nv);
        }

        // -----------------------------------------------------
        // C) Tạo KhachHang (5 bản ghi)
        List<KhachHang> listKH = new ArrayList<>();
        for(int i=1; i<=5; i++){
            KhachHang kh = new KhachHang(
                    "FAKE_KH" + i,
                    faker.address().streetAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.name().fullName()
            );
            khachHangDAO.save(kh);
            listKH.add(kh);
            System.out.println("Đã insert KhachHang: " + kh);
        }

        // -----------------------------------------------------
        // D) Tạo NhaCungCap (3 bản ghi)
        List<NhaCungCap> listNCC = new ArrayList<>();
        for(int i=1; i<=3; i++){
            NhaCungCap ncc = new NhaCungCap(
                    "FAKE_NCC" + i,
                    faker.address().fullAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.company().name()
            );
            nccDAO.save(ncc);
            listNCC.add(ncc);
            System.out.println("Đã insert NhaCungCap: " + ncc);
        }

        // -----------------------------------------------------
        // E) Tạo LoaiSP (3 bản ghi)
        List<LoaiSP> listLoai = new ArrayList<>();
        for(int i=1; i<=3; i++){
            LoaiSP lsp = new LoaiSP(
                    "FAKE_LOAI" + i,
                    "Loại " + faker.commerce().material() + i
            );
            loaiSPDAO.save(lsp);
            listLoai.add(lsp);
            System.out.println("Đã insert LoaiSP: " + lsp);
        }

        // -----------------------------------------------------
        // F) Tạo SanPham (5 bản ghi, random LoaiSP + NhaCungCap)
        List<SanPham> listSP = new ArrayList<>();
        for(int i=1; i<=5; i++){
            LoaiSP randomLoai = listLoai.get(rand.nextInt(listLoai.size()));
            NhaCungCap randomNCC = listNCC.get(rand.nextInt(listNCC.size()));

            SanPham sp = new SanPham(
                    "FAKE_SP" + i,
                    null, // Anh, tạm để null
                    faker.commerce().promotionCode(), // DonVi, vd. "promo200"
                    (double) faker.number().numberBetween(10_000, 1_000_000),
                    faker.number().numberBetween(10, 500),
                    faker.commerce().productName(),
                    randomLoai,
                    randomNCC
            );
            spDAO.save(sp);
            listSP.add(sp);
            System.out.println("Đã insert SanPham: " + sp);
        }

        // -----------------------------------------------------
        // G) Tạo HoaDon (5 bản ghi, random KhachHang + NhanVien)
        List<HoaDon> listHD = new ArrayList<>();
        for(int i=1; i<=5; i++){
            KhachHang randomKH = listKH.get(rand.nextInt(listKH.size()));
            NhanVien randomNV = listNV.get(rand.nextInt(listNV.size()));

            HoaDon hd = new HoaDon(
                    "FAKE_HD" + i,
                    LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)),
                    randomKH,
                    randomNV
            );
            hdDAO.save(hd);
            listHD.add(hd);
            System.out.println("Đã insert HoaDon: " + hd);
        }

        // -----------------------------------------------------
        // H) Tạo CT_HoaDon (mỗi HD -> random 2 chi tiết, random SP)
        //    Ở đây PK = (maSP, maHD, soLuong)
        for(HoaDon hd: listHD){
            // 2 CT_HoaDon
            for(int i=1; i<=2; i++){
                SanPham randomSP = listSP.get(rand.nextInt(listSP.size()));
                int sl = faker.number().numberBetween(1, 10);

                CTHoaDon cthd = new CTHoaDon(
                        randomSP.getMaSP(), // maSP
                        hd.getMaHD(),       // maHD
                        sl,                 // SoLuong
                        randomSP,           // ManyToOne
                        hd
                );
                cthdDAO.save(cthd);
                System.out.println("Đã insert CT_HoaDon: " + cthd);
            }
        }

        // -----------------------------------------------------
        // I) Tạo TaiKhoan (3 bản ghi, random NhanVien)
        for(int i=1; i<=3; i++){
            NhanVien randomNV = listNV.get(rand.nextInt(listNV.size()));
            TaiKhoan tk = new TaiKhoan(
                    "FAKE_TK" + i,
                    "pass" + i,           // MatKhau
                    "user" + i,           // TaiKhoan
                    "VaiTro" + i,         // VaiTro
                    randomNV              // NhanVien
            );
            tkDAO.save(tk);
            System.out.println("Đã insert TaiKhoan: " + tk);
        }

        System.out.println("=== HOÀN THÀNH sinh dữ liệu giả cho tất cả bảng. ===");

        // Đóng EMF
        emf.close();
    }
}