package application.test;

import application.dao.AccountDao;
import application.dao.TransactionDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
import application.dto.TransactionsDto;
import application.dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MbankTests {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    private final String testUsername = "tesztuser1";

    @Before
    public void setup() {
        userDao = new UserDao();
        accountDao = new AccountDao();
        transactionDao = new TransactionDao();

        // Előző példány törlése, ha létezett
        userDao.deleteByUserName(testUsername);

        // Új tesztfelhasználó mentése
        UserDto user = new UserDto(
            testUsername,
            "Teszt",
            "User",
            DigestUtils.sha1Hex("Jelszo123"),
            "teszt1@gmail.com",
            "+36201234567"
        );
        userDao.saveWithAccount(user);
    }

    @Test
    public void testUserRegistrationAndRetrieval() {
        UserDto retrieved = userDao.findByUserName(testUsername);

        assertNotNull("Felhasználónak léteznie kell", retrieved);
        assertEquals(testUsername, retrieved.getUserName());
        assertEquals("teszt1@gmail.com", retrieved.getEmail());
    }

    @Test
    public void testAccountRetrieval() {
        UserDto user = userDao.findByUserName(testUsername);
        assertNotNull("Felhasználó nem található", user);

        AccountsDto account = accountDao.findAccountByUserId(user.getId());
        assertNotNull("Számla nem található", account);
        assertEquals("HUF", account.getCurrency());
    }

    @Test
    public void testMonthlyIncomeAndExpense() {
        UserDto user = userDao.findByUserName(testUsername);
        assertNotNull("Felhasználó nem található", user);

        AccountsDto account = accountDao.findAccountByUserId(user.getId());
        assertNotNull("Számla nem található", account);

        double income = transactionDao.getMonthlyIncome(account.getId());
        double expense = transactionDao.getMonthlyExpense(account.getId());

        assertTrue("Bevétel nem lehet negatív", income >= 0);
        assertTrue("Kiadás nem lehet negatív", expense >= 0);
    }

    @Test
    public void testLatestTransactionsFetch() {
        UserDto user = userDao.findByUserName(testUsername);
        assertNotNull("Felhasználó nem található", user);

        AccountsDto account = accountDao.findAccountByUserId(user.getId());
        assertNotNull("Számla nem található", account);

        List<TransactionsDto> latest = transactionDao.getLatestTransactionsForAccount(account.getId(), 4);
        assertNotNull("Tranzakció lekérdezés nem lehet null", latest);
        assertTrue("Legfeljebb 4 tranzakció", latest.size() <= 4);
    }

    @After
    public void teardown() {
        UserDto user = userDao.findByUserName(testUsername);
        if (user != null) {
            accountDao.deleteByUserId(user.getId()); // először accounts törlése
            userDao.deleteByUserName("tesztuser1"); 
        }
    }

}
