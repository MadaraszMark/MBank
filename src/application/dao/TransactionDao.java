package application.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import application.dto.AccountsDto;
import application.dto.TransactionsDto;
import application.util.JpaUtil;

public class TransactionDao {

    public double getMonthlyIncome(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59);

        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT SUM(t.amount) FROM TransactionsDto t " +
                "WHERE t.toAccount = :accountId AND t.timeStamp BETWEEN :start AND :end", Double.class);
            query.setParameter("accountId", accountId);
            query.setParameter("start", startOfMonth);
            query.setParameter("end", endOfMonth);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            em.close();
        }
    }

    public double getMonthlyExpense(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59);

        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT SUM(t.amount) FROM TransactionsDto t " +
                "WHERE t.fromAccount = :accountId AND t.timeStamp BETWEEN :start AND :end", Double.class);
            query.setParameter("accountId", accountId);
            query.setParameter("start", startOfMonth);
            query.setParameter("end", endOfMonth);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            em.close();
        }
    }

    public void makeTransaction(Long fromId, Long toId, double amount, String currency, String message) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            AccountsDto fromAccount = em.find(AccountsDto.class, fromId);
            AccountsDto toAccount = em.find(AccountsDto.class, toId);

            if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Nincs elég fedezet a tranzakcióhoz.");
            }
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            em.merge(fromAccount);
            em.merge(toAccount);
            TransactionsDto transaction = new TransactionsDto(fromId, toId, amount, currency, LocalDateTime.now());
            transaction.setMessage(message);
            em.persist(transaction);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void transferAmount(Long fromAccountId, Long toAccountId, double amount, String message) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            AccountsDto from = em.find(AccountsDto.class, fromAccountId);
            AccountsDto to = em.find(AccountsDto.class, toAccountId);

            if (from == null || to == null) throw new RuntimeException("Számla nem található");

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            em.merge(from);
            em.merge(to);
            TransactionsDto t = new TransactionsDto(fromAccountId, toAccountId, amount, "HUF", LocalDateTime.now());
            t.setMessage(message);
            em.persist(t);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<TransactionsDto> getLatestTransactionsForAccount(Long accountId, int limit) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT t FROM TransactionsDto t WHERE t.fromAccount = :acc OR t.toAccount = :acc ORDER BY t.timeStamp DESC",
                    TransactionsDto.class)
                .setParameter("acc", accountId)
                .setMaxResults(limit)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    public List<TransactionsDto> getAllByAccountId(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                "SELECT t FROM TransactionsDto t WHERE t.fromAccount = :acc OR t.toAccount = :acc ORDER BY t.timeStamp DESC",
                TransactionsDto.class)
                .setParameter("acc", accountId)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
   
}

