/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controller;

/**
 *
 * @author borba
 */
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimento;

public class MovimentoJpaController implements Serializable {

    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) throws PreexistingEntityException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } catch (EntityExistsException e) {
            throw new PreexistingEntityException("Movimento " + movimento.getIDMovimento() + " já existe.", e);
        } finally {
            em.close();
        }
    }

    public void edit(Movimento movimento) throws NonexistentEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            movimento = em.merge(movimento);
            em.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            if (findMovimento(movimento.getIDMovimento()) == null) {
                throw new NonexistentEntityException("O movimento com id " + movimento.getIDMovimento() + " não existe.", e);
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Movimento movimento = em.getReference(Movimento.class, id);
            if (movimento == null) {
                throw new NonexistentEntityException("O movimento com id " + id + " não existe.");
            }
            em.remove(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Movimento findMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Movimento> findMovimentoEntities() {
        return findMovimentoEntities(true, -1, -1);
    }

    public List<Movimento> findMovimentoEntities(int maxResults, int firstResult) {
        return findMovimentoEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Movimento> findMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimento.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<Movimento> rt = cq.from(Movimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            return em.createQuery(cq).getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    public Integer findMaxId() {
        EntityManager em = getEntityManager();
        try {
            Integer max = em.createQuery(
                    "SELECT MAX(m.iDMovimento) FROM Movimento m", Integer.class)
                    .getSingleResult();
            return (max == null ? 0 : max);
        } finally {
            em.close();
        }
    }

}
