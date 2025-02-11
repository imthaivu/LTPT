package detai.app.dao;

import detai.app.entity.SanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class SanPhamDAO {
    private EntityManagerFactory emf;

    public SanPhamDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(SanPham sp) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(sp);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public SanPham findById(String maSP) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SanPham.class, maSP);
        } finally {
            em.close();
        }
    }

    public List<SanPham> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT s FROM SanPham s";
            TypedQuery<SanPham> query = em.createQuery(jpql, SanPham.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(SanPham sp) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(sp);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maSP) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SanPham found = em.find(SanPham.class, maSP);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}