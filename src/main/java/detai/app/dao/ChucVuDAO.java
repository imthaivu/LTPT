package detai.app.dao;

import detai.app.entity.ChucVu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ChucVuDAO {

    private EntityManagerFactory emf;

    public ChucVuDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(ChucVu cv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cv);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public ChucVu findById(String maCV) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ChucVu.class, maCV);
        } finally {
            em.close();
        }
    }

    public List<ChucVu> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM ChucVu c";
            TypedQuery<ChucVu> query = em.createQuery(jpql, ChucVu.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(ChucVu cv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cv);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maCV) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ChucVu found = em.find(ChucVu.class, maCV);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}