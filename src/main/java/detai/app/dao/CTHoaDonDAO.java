package detai.app.dao;

import detai.app.entity.CTHoaDon;
import detai.app.entity.CTHoaDonId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CTHoaDonDAO {
    private EntityManagerFactory emf;

    public CTHoaDonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // CREATE
    public void save(CTHoaDon cthd) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cthd);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // READ by composite key
    public CTHoaDon findById(CTHoaDonId id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(CTHoaDon.class, id);
        } finally {
            em.close();
        }
    }

    public List<CTHoaDon> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CTHoaDon> query = em.createQuery("SELECT c FROM CTHoaDon c", CTHoaDon.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(CTHoaDon cthd) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cthd);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(CTHoaDonId id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CTHoaDon found = em.find(CTHoaDon.class, id);
            if (found != null) {
                em.remove(found);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}