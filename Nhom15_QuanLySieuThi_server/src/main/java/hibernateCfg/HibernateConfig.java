package hibernateCfg;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import entity.CT_HoaDon;
import entity.CT_HoaDonPK;
import entity.ChucVu;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiSP;
import entity.NCC;
import entity.NhanVien;
import entity.SanPham;
import entity.TaiKhoan;


public class HibernateConfig {
    public static SessionFactory sessionFactory = null;
    public static HibernateConfig instance = new HibernateConfig();

    public static HibernateConfig getInstance() {
        return instance;
    }

    public HibernateConfig() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(ChucVu.class)
                .addAnnotatedClass(HoaDon.class)
                .addAnnotatedClass(KhachHang.class)
                .addAnnotatedClass(LoaiSP.class)
                .addAnnotatedClass(NCC.class)
                .addAnnotatedClass(NhanVien.class)
                .addAnnotatedClass(SanPham.class)
                .addAnnotatedClass(TaiKhoan.class)
                .addAnnotatedClass(CT_HoaDon.class)
                .addAnnotatedClass(CT_HoaDonPK.class)
                .getMetadataBuilder().build();
        if (sessionFactory == null) {
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void main(String[] args) {
        SessionFactory factory = HibernateConfig.getInstance().getSessionFactory();


    }
}
