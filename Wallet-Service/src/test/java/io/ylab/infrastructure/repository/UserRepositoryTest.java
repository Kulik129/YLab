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
        // Инициализация userRepository, через конструктор
        userRepository = new UserRepository("jdbc:postgresql://localhost:5432/y_lab", "root", "12345678");
    }
    @AfterEach
    void tearDown() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/y_lab", "root", "12345678")) {
            // Создайте и выполните SQL-запросы для удаления записей из базы данных


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
        // Создаем тестового пользователя и сохраняем его в базу данных
        User testUser = new User("uniqueLogin", "3342342", "4342424");
        userRepository.saveUserInDataBase(testUser);

        // Проверяем, что метод userPresence возвращает true для существующего пользователя
        assertTrue(userRepository.userPresence("3342342"));
    }

    @Test
    void testUserPresenceWhenUserDoesNotExist() {
        // Проверяем, что метод userPresence возвращает false для несуществующего пользователя
        assertFalse(userRepository.userPresence("nonexistentuser"));
    }

    @Test
    void testGetUserWhenUserExists() {
        User user = new User("Nik", "nik123", "123");
        userRepository.saveUserInDataBase(user);

        User retrievedUser = userRepository.getUser("nik123");

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
        // Создаем тестового пользователя и сохраняем его в базу данных
        User testUser = new User("Asti", "asti12", "8765");
        userRepository.saveUserInDataBase(testUser);

        // Изменяем баланс пользователя
        testUser.setBalance(150.0);
        userRepository.updateBalance(testUser);

        // Используем метод getUser, чтобы получить пользователя с обновленным балансом
        User retrievedUser = userRepository.getUser("asti12");

        // Проверяем, что баланс пользователя обновлен
        assertEquals(150.0, retrievedUser.getBalance(), 0.001); // Указываем допустимое отклонение для double
    }

    @Test
    void testAddTransaction() {
        // Создаем тестового пользователя и сохраняем его в базе данных
        User testUser = new User("Philopp", "pp12", "43432");
        userRepository.saveUserInDataBase(testUser);

        // Выполняем транзакцию для тестового пользователя
        userRepository.addTransaction(testUser, TransactionType.CREDIT, 50.0);

        // Получаем транзакции пользователя
        userRepository.getTransaction(testUser);

    }

    @Test
    void testGetTransaction() {
        // Создаем тестового пользователя и сохраняем его в базе данных
        User testUser = new User("Maxim", "galkin", "32132131");
        userRepository.saveUserInDataBase(testUser);

        // Выполняем транзакцию для тестового пользователя
        userRepository.addTransaction(testUser, TransactionType.DEBIT, 50.0);

        // Получаем транзакции пользователя
        userRepository.getTransaction(testUser);
    }
    @Test
    void testAddLogEntry() {
        // Создаем тестового пользователя и сохраняем его в базе данных
        User testUser = new User("Nikolai", "baskov", "3231321");
        userRepository.saveUserInDataBase(testUser);

        // Выполняем запись активности для тестового пользователя
        userRepository.addLogEntry(testUser, UserActions.AUTHORIZATION);
    }

}
