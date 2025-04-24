package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.KhachHang;
import entity.LoaiSP;
import entity.SanPham;
import entity.TaiKhoan;
import hibernateCfg.HibernateConfig;
import iRemote.ISanPham;

public class SanPhamDao extends UnicastRemoteObject implements ISanPham{

	private SessionFactory sessionFactory = null;

	/**
	 *
	 */

	public SanPhamDao() throws RemoteException {
		super();
		//TODO Auto-generated constructor stub
		sessionFactory    = HibernateConfig.getInstance().getSessionFactory();
	}

	@Override
	public List<SanPham> getAllSanPham() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<SanPham> listSP = session.createQuery("FROM SanPham", SanPham.class).getResultList();
			tr.commit();
			return listSP;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public SanPham getSP(String maSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			SanPham sanPham = session.find(SanPham.class, maSP);
			tr.commit();
			return sanPham;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean addSanPham(SanPham sanPham) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.save(sanPham);
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
	public boolean updateSanPham(SanPham sanPham) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.update(sanPham);
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
	public boolean deleteSanPham(String maSanPham) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.delete(session.find(SanPham.class, maSanPham));
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
	public List<SanPham> findSP(String sql) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			// Convert native SQL to HQL
			String hql = "FROM SanPham WHERE " + sql;
			List<SanPham> listSP = session.createQuery(hql, SanPham.class).getResultList();
			tr.commit();
			return listSP;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<SanPham> findSPTheoLoai(String loai) throws RemoteException {
		String sql = "SELECT sp FROM SanPham sp WHERE SanPham.loaiSP.id = :maloai";
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<SanPham> listSP = session.createQuery(sql, SanPham.class)
					.setParameter("maloai", loai)
					.getResultList();
			tr.commit();
			return listSP;
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
