package dao;

import entity.LoaiSP;
import hibernateCfg.HibernateConfig;
import iRemote.ILoaiSP;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoaiSPDao extends UnicastRemoteObject implements ILoaiSP{

	private SessionFactory sessionFactory = null;

	/**
	 *
	 */

	public LoaiSPDao() throws RemoteException {
		super();
		//TODO Auto-generated constructor stub
		sessionFactory   = HibernateConfig.getInstance().getSessionFactory();
	}

	@Override
	public List<LoaiSP> getAllLoaiSP() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<LoaiSP> listNCC = session.createNativeQuery("SELECT * FROM LoaiSP", LoaiSP.class).getResultList();
			tr.commit();
			return listNCC;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public LoaiSP getLoaiSP(String maLoaiSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			LoaiSP loaiSP = session.find(LoaiSP.class, maLoaiSP);
			tr.commit();
			return loaiSP;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean addLoaiSP(LoaiSP loaiSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.save(loaiSP);
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
	public boolean updateLoaiSP(LoaiSP loaiSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.update(loaiSP);
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
	public boolean deleteLoaiSP(String maLoaiSP) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.delete(session.find(LoaiSP.class, maLoaiSP));
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

	/**
	 *
	 */

}
