package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.CT_HoaDon;
import entity.KhachHang;
import entity.SanPham;
import hibernateCfg.HibernateConfig;
import iRemote.ICT_HoaDon;

public class CT_HoaDonDao extends UnicastRemoteObject implements ICT_HoaDon{

	private SessionFactory sessionFactory = null;

	/**
	 *
	 */

	public CT_HoaDonDao() throws RemoteException {
		super();
		//TODO Auto-generated constructor stub
		sessionFactory  = HibernateConfig.getInstance().getSessionFactory();
	}

	@Override
	public List<CT_HoaDon> getAllCTHoaDon() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon";
			List<CT_HoaDon> listKH = session.createQuery(hql, CT_HoaDon.class).getResultList();
			tr.commit();
			return listKH;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public CT_HoaDon getCTHoaDon(String maHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon cthd WHERE cthd.hoaDon.maHD = :maHD";
			CT_HoaDon ct_HoaDon = session.createQuery(hql, CT_HoaDon.class)
				.setParameter("maHD", maHD)
				.getSingleResult();
			tr.commit();
			return ct_HoaDon;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean addCTHD(CT_HoaDon ctHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.save(ctHD);
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
	public boolean updateCTHD(CT_HoaDon ctHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.update(ctHD);
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
	public boolean deleteCTHD(String maCTHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.delete(session.find(CT_HoaDon.class, maCTHD));
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
	public void deleteSPHoaDon(String maHD, String maSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "DELETE FROM CT_HoaDon cthd WHERE cthd.hoaDon.maHD = :maHD AND cthd.sanPham.maSP = :maSP";
			session.createQuery(hql)
				.setParameter("maHD", maHD)
				.setParameter("maSP", maSP)
				.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public boolean checkSPHoaDon(String maHD, String maSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon cthd WHERE cthd.hoaDon.maHD = :maHD AND cthd.sanPham.maSP = :maSP";
			CT_HoaDon ctHD = session.createQuery(hql, CT_HoaDon.class)
				.setParameter("maHD", maHD)
				.setParameter("maSP", maSP)
				.getSingleResult();
			tr.commit();
			return ctHD != null;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public List<CT_HoaDon> getCTHDByMaHD(String maHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon WHERE hoaDon.maHD = :maHD";
			List<CT_HoaDon> listCTHD = session.createQuery(hql, CT_HoaDon.class)
					.setParameter("maHD", maHD)
					.getResultList();
			tr.commit();
			return listCTHD;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<CT_HoaDon> getCTHDForReport(String maHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon cthd WHERE cthd.hoaDon.maHD = :maHD";
			List<CT_HoaDon> listCTHD = session.createQuery(hql, CT_HoaDon.class)
				.setParameter("maHD", maHD)
				.getResultList();
			tr.commit();
			return listCTHD;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<CT_HoaDon> findCTHDByMaHD(String maHD) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM CT_HoaDon WHERE hoaDon.maHD = :maHD";
			List<CT_HoaDon> listCTHD = session.createQuery(hql, CT_HoaDon.class)
					.setParameter("maHD", maHD)
					.getResultList();
			tr.commit();
			return listCTHD;
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
