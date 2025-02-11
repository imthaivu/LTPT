package detai.app.dao;

import detai.app.entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class KhachHangDAO {
    private EntityManagerFactory emf;

    public KhachHangDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(KhachHang kh) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(kh);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public KhachHang findById(String maKH) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(KhachHang.class, maKH);
        } finally {
            em.close();
        }
    }

    public List<KhachHang> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT k FROM KhachHang k";
            TypedQuery<KhachHang> query = em.createQuery(jpql, KhachHang.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(KhachHang kh) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(kh);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maKH) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            KhachHang found = em.find(KhachHang.class, maKH);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}