package dao;

import entity.HoaDon;
import hibernateCfg.HibernateConfig;
import iRemote.IHoaDon;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class HoaDonDao extends UnicastRemoteObject implements IHoaDon {

    private SessionFactory sessionFactory = null;

    public HoaDonDao() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
        sessionFactory = HibernateConfig.getInstance().getSessionFactory();
    }

    @Override
    public List<HoaDon> getAllHD() throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            List<HoaDon> listHD = session.createNativeQuery("SELECT * FROM HoaDon", HoaDon.class).getResultList();
            tr.commit();
            return listHD;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public HoaDon getHD(String maHD) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            HoaDon hoaDon = session.find(HoaDon.class, maHD);
            tr.commit();
            return hoaDon;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<HoaDon> findHD(String sql) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            List<HoaDon> listHD = session.createNativeQuery(sql, HoaDon.class).getResultList();
            tr.commit();
            return listHD;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean addHoaDon(HoaDon hoaDon) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.save(hoaDon);
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
    public boolean updateHoaDon(HoaDon hoaDon) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.update(hoaDon);
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
    public boolean deleteHoaDon(String maHoaDon) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.delete(session.find(HoaDon.class, maHoaDon));
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
    public boolean deleteSanPham(String maHoaDon) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }


}
