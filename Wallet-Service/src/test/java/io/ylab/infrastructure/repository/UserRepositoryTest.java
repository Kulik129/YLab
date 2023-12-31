package io.ylab.infrastructure.repository;

import config.ContainersEnvironment;
import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends ContainersEnvironment {
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }
    @AfterEach
    void tearDown() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/y_lab", "root", "12345678")) {
            String deleteActionsQuery = "DELETE FROM data.actions WHERE user_id>3";
            String deleteTransactionsQuery = "DELETE FROM data.transactions WHERE user_id>3";
            String deleteUsersQuery = "DELETE FROM data.users WHERE id>3";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteActionsQuery);
                statement.executeUpdate(deleteTransactionsQuery);
                statement.executeUpdate(deleteUsersQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUserPresenceWhenUserExists() {
        User testUser = new User("uniqueLogin", "3342342", "4342424");
        userRepository.saveUser(testUser);

        assertTrue(userRepository.userPresence("3342342"));
    }

    @Test
    void testUserPresenceWhenUserDoesNotExist() {
        assertFalse(userRepository.userPresence("nonexistentuser"));
    }

    @Test
    void testGetUserWhenUserExists() {
        User user = new User("Nik", "nik123", "123");
        userRepository.saveUser(user);

        User retrievedUser = userRepository.findByLogin("nik123");

        assertNotNull(retrievedUser);
        assertEquals("nik123", retrievedUser.getLogin());
        assertEquals("123", retrievedUser.getPassword());
    }

    @Test
    void testUserPresence() {
        assertTrue(userRepository.userPresence("admin"));
    }

    @Test
    void testUpdateBalance() {
        User testUser = new User("Asti", "asti12", "8765");
        userRepository.saveUser(testUser);

        testUser.setBalance(150.0);
        userRepository.updateBalance(testUser);

        User retrievedUser = userRepository.findByLogin("asti12");

        assertEquals(150.0, retrievedUser.getBalance(), 0.001); // Указываем допустимое отклонение для double
    }

    @Test
    void testAddTransaction() {
        User testUser = new User("Philopp", "pp12", "43432");
        userRepository.saveUser(testUser);

        userRepository.saveTransaction(testUser, TransactionType.CREDIT, 50.0);

        userRepository.getTransaction(testUser);

    }

    @Test
    void testGetTransaction() {
        User testUser = new User("Maxim", "galkin", "32132131");
        userRepository.saveUser(testUser);

        userRepository.saveTransaction(testUser, TransactionType.DEBIT, 50.0);

        userRepository.getTransaction(testUser);
    }
    @Test
    void testAddLogEntry() {
        User testUser = new User("Nikolai", "baskov", "3231321");
        userRepository.saveUser(testUser);

        userRepository.saveAction(testUser, UserActions.AUTHORIZATION);
    }

}
