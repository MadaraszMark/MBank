package application.dao;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import application.dto.AccountsDto;
import application.dto.LoginLogDto;
import application.dto.UserDto;
import application.util.JpaUtil;

public class UserDao {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Long validateUserByUserNameAndPassword(String userName, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(Long.class);
            Root<UserDto> from = cq.from(UserDto.class);
            cq.select(cb.count(from)).where(cb.and(
                    cb.equal(from.get("userName"), userName),
                    cb.equal(from.get("password"), password)
            ));

            TypedQuery<Long> typedQuery = em.createQuery(cq);
            return typedQuery.getSingleResult();
        } finally {
            em.close();
        }
    }

    public boolean emailExists(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<UserDto> root = cq.from(UserDto.class);
            cq.select(cb.count(root)).where(cb.equal(root.get("email"), email));
            return em.createQuery(cq).getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public boolean phoneExists(String phoneNumber) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<UserDto> root = cq.from(UserDto.class);
            cq.select(cb.count(root)).where(cb.equal(root.get("phoneNumber"), phoneNumber));
            return em.createQuery(cq).getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public void save(UserDto userDto) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(userDto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private String generateAccountNumber() {
        return "MB" + (long) (Math.random() * 1_000_000_000L);
    }

    public static String generateCardNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int group = 1000 + rand.nextInt(9000);
            sb.append(group);
            if (i < 3) sb.append(" ");
        }
        return sb.toString();
    }

    public void saveWithAccount(UserDto userDto) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(userDto);
            em.flush();

            AccountsDto account = new AccountsDto(
                    generateAccountNumber(),
                    "checking",
                    0.0,
                    "HUF",
                    "active",
                    LocalDateTime.now(),
                    generateCardNumber()
            );
            account.setUserId(userDto.getId());
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public String getFirstNameByUsername(String userName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT u.firstName FROM UserDto u WHERE u.userName = :userName", String.class);
            query.setParameter("userName", userName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return "Ismeretlen";
        } finally {
            em.close();
        }
    }

    public void logLoginAttempt(Long userId, boolean success) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            LoginLogDto log = new LoginLogDto(LocalDateTime.now(), ipAddress, success);
            log.setUserId(userId);
            em.persist(log);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public UserDto findByUserName(String userName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<UserDto> cq = cb.createQuery(UserDto.class);
            Root<UserDto> root = cq.from(UserDto.class);
            cq.select(root).where(cb.equal(root.get("userName"), userName));
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public UserDto findUserById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(UserDto.class, id);
        } finally {
            em.close();
        }
    }
    
    public void update(UserDto userDto) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(userDto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    public List<UserDto> findAllUsers() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserDto u", UserDto.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public void deleteByUserId(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            UserDto user = em.find(UserDto.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteByUserName(String userName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<UserDto> query = em.createQuery(
                "SELECT u FROM UserDto u WHERE u.userName = :userName", UserDto.class);
            query.setParameter("userName", userName);
            
            List<UserDto> result = query.getResultList();
            for (UserDto user : result) {
                em.remove(user);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }




}

