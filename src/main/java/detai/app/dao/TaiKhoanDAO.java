package detai.app.dao;

import detai.app.entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class TaiKhoanDAO {
    private EntityManagerFactory emf;

    public TaiKhoanDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(TaiKhoan tk) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tk);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public TaiKhoan findById(String maTK) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TaiKhoan.class, maTK);
        } finally {
            em.close();
        }
    }

    public List<TaiKhoan> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM TaiKhoan t";
            TypedQuery<TaiKhoan> query = em.createQuery(jpql, TaiKhoan.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(TaiKhoan tk) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tk);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maTK) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TaiKhoan found = em.find(TaiKhoan.class, maTK);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}