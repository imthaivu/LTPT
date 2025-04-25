package dao;

import entity.CT_HoaDon;
import hibernateCfg.HibernateConfig;
import iRemote.ICT_HoaDon;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
			List<CT_HoaDon> listKH = session.createNativeQuery("SELECT * FROM CT_HOADON", CT_HoaDon.class).getResultList();
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
			CT_HoaDon ct_HoaDon = session.createNativeQuery("SELECT * FROM CT_HoaDon WHERE MaHD = '" + maHD + "'", CT_HoaDon.class).getSingleResult();
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
	public List<CT_HoaDon> findCTHD(String sql) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<CT_HoaDon> listCTHD = session.createNativeQuery(sql, CT_HoaDon.class).getResultList();
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSPHoaDon(String maHD, String maSP) throws RemoteException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.createNativeQuery("DELETE FROM CT_HoaDon WHERE MaHD = '" + maHD + "' AND MaSP = '" + maSP + "'", CT_HoaDon.class);
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
			CT_HoaDon ctHD = session.createNativeQuery("SELECT * FROM CT_HOADON WHERE MaHD= '" + maHD + "'" +  "AND MaSP= '" + maSP + "'", CT_HoaDon.class).getSingleResult();
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

	/**
	 *
	 */


}
