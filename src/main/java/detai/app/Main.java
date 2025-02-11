package detai.app;

import detai.app.entity.ChucVu;
import detai.app.entity.NhanVien;
import net.datafaker.Faker;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // 1) Tạo EMF từ persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");
        EntityManager em = emf.createEntityManager();

        // 2) Faker
        Faker faker = new Faker();

        try {
            em.getTransaction().begin();

            // Thêm 2 ChucVu
            for (int i = 1; i <= 2; i++) {
                ChucVu cv = new ChucVu();
                cv.setMaCV("FAKE_CV" + i);
                cv.setLuong((double) faker.number().numberBetween(5_000_000, 20_000_000));
                cv.setTenCV(faker.job().title());

                em.persist(cv);
            }

            // Lấy ChucVu FAKE_CV1 để gán cho NhanVien
            ChucVu cv1 = em.find(ChucVu.class, "FAKE_CV1");
            if (cv1 != null) {
                for (int i = 1; i <= 2; i++) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNv("FAKE_NV" + i);
                    nv.setHoTen(faker.name().fullName());
                    nv.setGioiTinh(faker.bool().bool());
                    nv.setCaLamViec("Ca " + i);
                    nv.setLuong((double) faker.number().numberBetween(8_000_000, 15_000_000));
                    nv.setNgaySinh(LocalDateTime.now().minusYears(faker.number().numberBetween(20, 40)));
                    nv.setSoDT(faker.phoneNumber().phoneNumber());
                    nv.setTrangThai("Đang làm");
                    nv.setChucVu(cv1);

                    em.persist(nv);
                }
            }

            em.getTransaction().commit();

            // 3) In ra danh sách NhanVien
            TypedQuery<NhanVien> query = em.createQuery("SELECT n FROM NhanVien n", NhanVien.class);
            query.getResultList().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}