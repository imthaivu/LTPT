package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.TaiKhoan;
import hibernateCfg.HibernateConfig;
import iRemote.ITaiKhoan;

public class TaiKhoanDao extends UnicastRemoteObject implements ITaiKhoan{

	/**
	 *
	 */
	private SessionFactory sessionFactory = null;

	public TaiKhoanDao() throws RemoteException {
		sessionFactory = HibernateConfig.getInstance().getSessionFactory();
	}

	/**
	 *
	 */


	@Override
	public TaiKhoan getTaiKhoan(String taiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			TaiKhoan khoan = session.find(TaiKhoan.class, taiKhoan);
			tr.commit();
			return khoan;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean themTaiKhoan(TaiKhoan taiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.save(taiKhoan);
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
	public boolean suaTaiKhoan(TaiKhoan taiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.update(taiKhoan);
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
	public boolean xoaTaiKhoan(String tenTaiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.delete(session.find(TaiKhoan.class, tenTaiKhoan));
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
	public TaiKhoan getTaiKhoanByUserNamePassword(String taiKhoan, String matKhau) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			String sql = "SELECT tk FROM TaiKhoan tk WHERE tk.taiKhoan = :taiKhoan AND tk.matKhau = :matKhau";
			TaiKhoan tk = session.createQuery(sql, TaiKhoan.class)
				.setParameter("taiKhoan", taiKhoan)
				.setParameter("matKhau", matKhau)
				.getSingleResult();
			tr.commit();
			return tk;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<TaiKhoan> getAllTaiKhoan() throws RemoteException {
		String sql = "SELECT tk FROM TaiKhoan tk";
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			List<TaiKhoan> listTK = session.createQuery(sql, TaiKhoan.class).getResultList();
			tr.commit();
			return listTK;
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public void changePassWord(String userName, String oldPassWord, String newPassword) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			TaiKhoan tk = getTaiKhoanByUserNamePassword(userName, oldPassWord);
			if (tk != null) {
				tk.setMatKhau(newPassword);
				session.update(tk);
				tr.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public boolean addTaiKhoan(TaiKhoan taiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.save(taiKhoan);
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
	public boolean updateTaiKhoan(TaiKhoan taiKhoan) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			session.update(taiKhoan);
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
	public boolean deleteTaiKhoan(String maTK) throws RemoteException {
		Session session = sessionFactory.openSession();
		Transaction tr = session.getTransaction();
		try {
			tr.begin();
			TaiKhoan taiKhoan = session.get(TaiKhoan.class, maTK);
			if (taiKhoan != null) {
				session.delete(taiKhoan);
				tr.commit();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tr.rollback();
		} finally {
			session.close();
		}
		return false;
	}

}
