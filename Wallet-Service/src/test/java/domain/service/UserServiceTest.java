package domain.service;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;
import io.ylab.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
        testUser = new User(1, "Vik", "test", "password");
        userService.userMap.put("test", testUser);
    }

    @Test
    public void testRegistrationUser() {
        userService.registrationUser("John", "john123", "password");
        User registeredUser = userService.userMap.get("john123");
        assertNotNull(registeredUser);
        assertEquals("John", registeredUser.getName());
    }

    @Test
    public void testAuthorizationUser() {
        userService.userMap.put("test", testUser);

        userService.authorizationUser("test", "password");
        assertTrue(userService.listActions.stream()
                .anyMatch(log -> log.getUserActions() == UserActions.AUTHORIZATION));


        userService.authorizationUser("test", "wrongpassword");
        assertFalse(userService.listActions.stream()
                .anyMatch(log -> log.getUserActions() == UserActions.FATAL));
    }

    @Test
    public void testCurrentBalance() {
        double balance = userService.currentBalance(testUser);
        assertEquals(0.0, balance);

        testUser.setBalance(100.0);
        balance = userService.currentBalance(testUser);
        assertEquals(100.0, balance);
    }

    @Test
    public void testWithdrawalOfFunds() {
        testUser.setBalance(100.0);

        userService.withdrawalOfFunds("test", 50.0);
        assertEquals(50.0, testUser.getBalance());
        assertTrue(userService.listActions.stream()
                .anyMatch(log -> log.getUserActions() == UserActions.CREDIT));


        userService.withdrawalOfFunds("test", 200.0);
        assertEquals(50.0, testUser.getBalance());
        assertTrue(userService.listActions.stream()
                .anyMatch(log -> log.getUserActions() == UserActions.FATAL));
    }

    @Test
    public void testBalanceReplenishment() {
        testUser.setBalance(100.0);

        userService.balanceReplenishment("test", 50.0);
        assertEquals(150.0, testUser.getBalance());
        assertTrue(userService.listActions.stream()
                .anyMatch(log -> log.getUserActions() == UserActions.DEBIT));
    }

    @Test
    public void testReplenishmentHistory() {
        testUser.setBalance(100.0);

        List<Transaction> transactions = userService.replenishmentHistory(testUser, TransactionType.CREDIT, 50.0);
        assertEquals(1, transactions.size());
        assertEquals(50.0, transactions.get(0).getSum());
    }

    @Test
    public void testAuditOfActions() {
        List<LogEntry> actions = userService.auditOfActions(testUser, UserActions.REGISTRATION);
        assertEquals(1, actions.size());
        assertEquals(UserActions.REGISTRATION, actions.get(0).getUserActions());
    }
}
