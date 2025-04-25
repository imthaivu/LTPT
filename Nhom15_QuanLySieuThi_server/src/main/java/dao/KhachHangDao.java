package dao;

import entity.KhachHang;
import hibernateCfg.HibernateConfig;
import iRemote.IKhachHang;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class KhachHangDao extends UnicastRemoteObject implements IKhachHang {

    private SessionFactory sessionFactory = null;

    public KhachHangDao() throws RemoteException {
        sessionFactory = HibernateConfig.getInstance().getSessionFactory();
    }


    @Override
    public List<KhachHang> getAllKH() throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            List<KhachHang> listKH = session.createNativeQuery("SELECT * FROM KhachHang", KhachHang.class).getResultList();
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
    public boolean addCustomer(KhachHang customer) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.save(customer);
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
    public boolean updateCustomer(KhachHang customer) throws RemoteException {
        // TODO Auto-generated method stub
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.update(customer);
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
    public KhachHang getKhachHang(String maKH) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            KhachHang kh = session.find(KhachHang.class, maKH);
            tr.commit();
            return kh;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return null;
    }


    @Override
    public List<KhachHang> findKH(String sql) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            List<KhachHang> listKH = session.createNativeQuery(sql, KhachHang.class).getResultList();
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


}
