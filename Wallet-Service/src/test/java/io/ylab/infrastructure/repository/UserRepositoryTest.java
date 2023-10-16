package io.ylab.infrastructure.repository;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void getUserMap() {
        User user = new User(1, "Name", "name", "1234");
        userRepository.saveUserInDataBase(user);

        assertTrue(userRepository.userPresence("name"));
        assertEquals(user, userRepository.getUser("name"));
    }

    @Test
    void addUserInMap() {
        User user = new User(1, "TestUser", "test", "password");
        userRepository.saveUserInDataBase(user);

        assertTrue(userRepository.userPresence("test"));
        assertEquals(user, userRepository.getUser("test"));
    }

    @Test
    void getUser() {
        User user = new User(2,"Dima", "dim123","1234");
        userRepository.saveUserInDataBase(user);

        // Вызываем метод getUser для получения пользователя
        User retrievedUser = userRepository.getUser("dim123");

        // Проверяем, что полученный пользователь совпадает с добавленным
        assertEquals(user, retrievedUser);
    }

    @Test
    void testGetUser_NonExistentUser() {
        // Попытка получения пользователя, которого нет в мапе
        User retrievedUser = userRepository.getUser("nonexistent");

        // Проверяем, что полученный пользователь равен null
        assertNull(retrievedUser);
    }

    @Test
    void userPresence() {
        User user = new User(3,"Alisa", "al23", "4321");
        userRepository.saveUserInDataBase(user);
        assertTrue(userRepository.userPresence("al23"));
    }

    @Test
    void addTransaction() {
        User user = userRepository.getUser("admin");
        userRepository.addTransaction(user, TransactionType.CREDIT, 100.0);

        assertFalse(user.getTransactions().isEmpty());
        assertEquals(1, user.getTransactions().size());
        assertEquals(TransactionType.CREDIT, user.getTransactions().get(0).getTransactionType());
        assertEquals(100.0, user.getTransactions().get(0).getSum(), 0.001);
    }

    @Test
    void getTransaction() {
        User user = new User(1,"Ivan", "ivan34", "3434");
        Transaction transaction = new Transaction(3, user, TransactionType.DEBIT, 300, LocalDateTime.now());
        user.getTransactions().add(transaction);

        userRepository.getTransaction(user);
    }

    @Test
    void addLogEntry() {
        User user = userRepository.getUser("admin");
        userRepository.addLogEntry(user, UserActions.AUTHORIZATION);

        assertFalse(user.getActionLog().isEmpty());
        assertEquals(1, user.getActionLog().size());
        assertEquals(UserActions.AUTHORIZATION, user.getActionLog().get(0).getUserActions());
    }

    @Test
    void getLogEntry() {
        User user = new User(2,"Ilya", "ilya23", "54545");
        LogEntry log = new LogEntry(4,UserActions.FATAL, LocalDateTime.now(), user);
        user.getActionLog().add(log);

        userRepository.getLogEntry(user);
    }

    @Test
    void testGetListActions() {
        User user = new User(2,"Katy", "kk34", "54");
        User user2 = new User(2,"Sofi", "ss12", "9876");


        LogEntry logEntry1 = new LogEntry(1, UserActions.DEBIT, LocalDateTime.now(), user);
        LogEntry logEntry2 = new LogEntry(2, UserActions.LOGOUT, LocalDateTime.now(), user2);

        userRepository.getListActions().add(logEntry1);
        userRepository.getListActions().add(logEntry2);

        List<LogEntry> actions = userRepository.getListActions();

        assertEquals(2, actions.size());
        assertTrue(actions.contains(logEntry1));
        assertTrue(actions.contains(logEntry2));
    }
}
