package application.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import application.dto.AccountsDto;
import application.util.JpaUtil;

public class AccountDao {

    public AccountsDto findAccountByUserId(Long userId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<AccountsDto> query = em.createQuery(
                "SELECT a FROM AccountsDto a WHERE a.userId = :userId", AccountsDto.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Nincs számla az adott userId-hoz: " + userId);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public AccountsDto findByAccountNumber(String accountNumber) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<AccountsDto> query = em.createQuery(
                "SELECT a FROM AccountsDto a WHERE a.accountNumber = :accountNumber", AccountsDto.class);
            query.setParameter("accountNumber", accountNumber);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Nincs ilyen számlaszám: " + accountNumber);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public AccountsDto findAccountById(Long accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(AccountsDto.class, accountId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
    public void updateBalance(Long accountId, double newBalance) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            AccountsDto account = em.find(AccountsDto.class, accountId);

            if (account != null) {
                account.setBalance(newBalance);
                em.merge(account);
                em.getTransaction().commit();
            } else {
                System.out.println("Számla nem található frissítéshez.");
                em.getTransaction().rollback();
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteByUserId(Long userId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM AccountsDto a WHERE a.userId = :userId")
              .setParameter("userId", userId)
              .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}