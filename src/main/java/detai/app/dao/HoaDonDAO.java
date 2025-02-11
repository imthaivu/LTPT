package detai.app.dao;

import detai.app.entity.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class HoaDonDAO {
    private EntityManagerFactory emf;

    public HoaDonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(HoaDon hd) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hd);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public HoaDon findById(String maHD) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(HoaDon.class, maHD);
        } finally {
            em.close();
        }
    }

    public List<HoaDon> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT h FROM HoaDon h";
            TypedQuery<HoaDon> query = em.createQuery(jpql, HoaDon.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(HoaDon hd) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(hd);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maHD) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            HoaDon found = em.find(HoaDon.class, maHD);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}