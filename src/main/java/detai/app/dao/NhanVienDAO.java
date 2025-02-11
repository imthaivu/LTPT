package detai.app.dao;

import detai.app.entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class NhanVienDAO {
    private EntityManagerFactory emf;

    public NhanVienDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(NhanVien nv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nv);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public NhanVien findById(String maNv) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(NhanVien.class, maNv);
        } finally {
            em.close();
        }
    }

    public List<NhanVien> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT n FROM NhanVien n";
            TypedQuery<NhanVien> query = em.createQuery(jpql, NhanVien.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(NhanVien nv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(nv);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String maNv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            NhanVien found = em.find(NhanVien.class, maNv);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}