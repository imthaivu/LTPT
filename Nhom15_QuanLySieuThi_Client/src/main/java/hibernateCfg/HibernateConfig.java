package hibernateCfg;


import entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;


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
		SessionFactory factory= HibernateConfig.getInstance().getSessionFactory();
		
		
	}
}
