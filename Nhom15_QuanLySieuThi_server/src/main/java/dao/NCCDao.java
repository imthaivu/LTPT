package dao;

import entity.NCC;
import hibernateCfg.HibernateConfig;
import iRemote.INCC;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NCCDao extends UnicastRemoteObject implements INCC {

    private SessionFactory sessionFactory = null;


    public NCCDao() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
        sessionFactory = HibernateConfig.getInstance().getSessionFactory();
    }

    @Override
    public List<NCC> getAllNCC() throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            List<NCC> listNCC = session.createNativeQuery("SELECT * FROM NhaCungCap", NCC.class).getResultList();
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
    public NCC getNCC(String maNCC) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            NCC ncc = session.find(NCC.class, maNCC);
            tr.commit();
            return ncc;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean addNCC(NCC ncc) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.save(ncc);
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
    public boolean updateNCC(NCC ncc) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.update(ncc);
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
    public boolean deleteNCC(String maNcc) throws RemoteException {
        Session session = sessionFactory.openSession();
        Transaction tr = session.getTransaction();
        try {
            tr.begin();
            session.delete(session.find(NCC.class, maNcc));
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


}
