package dao;

import entity.NhanVien;
import hibernateCfg.HibernateConfig;
import iRemote.INhanVien;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NhanVienDao extends UnicastRemoteObject implements INhanVien{

	private SessionFactory sessionFactory;

	/**
	 *
	 */

	/**
	 *
	 */
	public NhanVienDao() throws RemoteException {
		super();
		//TODO Auto-generated constructor stub
		sessionFactory = HibernateConfig.getInstance().getSessionFactory();
	}

	@Override
	public List<NhanVien> getAllNV() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<NhanVien> listNV = session.createNativeQuery("  SELECT * FROM NhanVien where TrangThai = N'Đang làm'", NhanVien.class).getResultList();
			tr.commit();
			return listNV;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}


	@Override
	public NhanVien getNhanVien(String maNV) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				NhanVien nv = session.find(NhanVien.class, maNV);
			tr.commit();
			return nv;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean addNhanVien(NhanVien nhanVien) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				session.save(nhanVien);
			tr.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public boolean updateNhanVien(NhanVien nhanVien) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				session.update(nhanVien);
			tr.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public List<NhanVien> getAllNVNghi() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<NhanVien> listNV = session.createNativeQuery("SELECT * FROM NhanVien WHERE TrangThai LIKE N'Nghỉ việc'", NhanVien.class).getResultList();
			tr.commit();
			return listNV;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<NhanVien> getNVByChucVu(String maCV) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<NhanVien> listNV = session.createNativeQuery("SELECT * FROM NhanVien WHERE chucVu_MaCV = '" + maCV + "'", NhanVien.class).getResultList();
			tr.commit();
			return listNV;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 *
	 */

}
