package detai.app.dao;

import detai.app.entity.LoaiSP;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class LoaiSPDAO {
    private EntityManagerFactory emf;

    public LoaiSPDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(LoaiSP lsp) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(lsp);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public LoaiSP findById(String maLoaiSP) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(LoaiSP.class, maLoaiSP);
        } finally {
            em.close();
        }
    }

    public List<LoaiSP> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT l FROM LoaiSP l";
            TypedQuery<LoaiSP> query = em.createQuery(jpql, LoaiSP.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(LoaiSP lsp) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(lsp);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maLoaiSP) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            LoaiSP found = em.find(LoaiSP.class, maLoaiSP);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}