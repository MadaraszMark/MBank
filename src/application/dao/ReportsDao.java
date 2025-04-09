package application.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import application.dto.ReportsDto;
import application.util.JpaUtil;

public class ReportsDao {

    public void saveReport(ReportsDto report) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(report);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<ReportsDto> findAllReports() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<ReportsDto> query = em.createQuery("SELECT r FROM ReportsDto r ORDER BY r.submittedAt DESC", ReportsDto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
