package detai.app.dao;

import detai.app.entity.NhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class NhaCungCapDAO {
    private EntityManagerFactory emf;

    public NhaCungCapDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(NhaCungCap ncc) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ncc);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public NhaCungCap findById(String maNCC) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(NhaCungCap.class, maNCC);
        } finally {
            em.close();
        }
    }

    public List<NhaCungCap> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT n FROM NhaCungCap n";
            TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(NhaCungCap ncc) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ncc);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maNCC) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            NhaCungCap found = em.find(NhaCungCap.class, maNCC);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}