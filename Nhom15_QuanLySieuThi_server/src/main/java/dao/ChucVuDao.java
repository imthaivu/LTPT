package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.ChucVu;
import entity.KhachHang;
import hibernateCfg.HibernateConfig;
import iRemote.IChucVu;

public class ChucVuDao extends UnicastRemoteObject implements IChucVu{

	/**
	 *
	 */
	private static final long serialVersionUID = 3533103237829692666L;
	private SessionFactory sessionFactory = null;

	public ChucVuDao() throws RemoteException {
		super();
		sessionFactory  = HibernateConfig.getInstance().getSessionFactory();
		//TODO Auto-generated constructor stub
	}

	@Override
	public List<ChucVu> getAllCV() throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String hql = "FROM ChucVu";
			List<ChucVu> listCV = session.createQuery(hql, ChucVu.class).getResultList();
			tr.commit();
			return listCV;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public ChucVu getCV(String maCV) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				ChucVu cv = session.find(ChucVu.class, maCV);
			tr.commit();
			return cv;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean addChucVu(ChucVu chucVu) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				session.save(chucVu);
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
	public boolean updateChucVu(ChucVu chucVu) throws RemoteException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				session.update(chucVu);
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
	public boolean deleteChucVu(ChucVu chucVu) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
				session.delete(chucVu);
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
