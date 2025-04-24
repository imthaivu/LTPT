package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.NhanVien;
import hibernateCfg.HibernateConfig;
import iRemote.INhanVien;

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
		String sql = "SELECT nv FROM NhanVien nv";
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<NhanVien> listNV = session.createQuery(sql, NhanVien.class).getResultList();
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
	public NhanVien getNV(String maNV) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			NhanVien nhanVien = session.get(NhanVien.class, maNV);
			tr.commit();
			return nhanVien;
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
			String hql = "FROM NhanVien nv WHERE nv.trangThai = :trangThai";
			List<NhanVien> listNV = session.createQuery(hql, NhanVien.class)
				.setParameter("trangThai", "Nghỉ việc")
				.getResultList();
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
			String hql = "FROM NhanVien nv WHERE nv.chucVu.maCV = :maCV";
			List<NhanVien> listNV = session.createQuery(hql, NhanVien.class)
				.setParameter("maCV", maCV)
				.getResultList();
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
