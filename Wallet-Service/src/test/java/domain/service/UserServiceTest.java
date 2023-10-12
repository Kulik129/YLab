package domain.service;

import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.User;
import io.ylab.domain.service.UserService;
import io.ylab.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegistrationUser() {
        // Подготовка макета UserRepository
        User user = new User(1, "Лена", "lenok", "password123");
        when(userRepository.userPresence("lenok")).thenReturn(false);
        when(userRepository.getUserMap()).thenReturn(new HashMap<>());

        // Вызов метода для тестирования
        boolean result = userService.registrationUser("lenok", "password123", "Лена");

        // Проверка результата
        assertTrue(result);

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addUserInMap(user);
        verify(userRepository, times(1)).addLogEntry(eq(user), eq(UserActions.REGISTRATION));
    }
    @Test
    public void testAuthorizationUser_ValidCredentials() {
        // Подготовка макета UserRepository
        String login = "vik";
        String password = "432";
        User user = new User(1, "Viktor", login, password);
        when(userRepository.userPresence(login)).thenReturn(true);
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        boolean result = userService.authorizationUser(login, password);

        // Проверка результата
        assertTrue(result);

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addLogEntry(user, UserActions.AUTHORIZATION);
    }

    @Test
    public void testAuthorizationUser_InvalidCredentials() {
        // Подготовка макета UserRepository
        String login = "misha";
        String password = "bbdfbdf";
        User user = new User(1, "Test User", login, "correct");
        when(userRepository.userPresence(login)).thenReturn(true);
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        boolean result = userService.authorizationUser(login, password);

        // Проверка результата
        assertFalse(result);

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addLogEntry(user, UserActions.FATAL);
    }

    @Test
    public void testAuthorizationUser_UserNotFound() {
        // Подготовка макета UserRepository
        String login = "non-existent user";
        when(userRepository.userPresence(login)).thenReturn(false);

        // Вызов метода для тестирования
        boolean result = userService.authorizationUser(login, "password123");

        // Проверка результата
        assertFalse(result);
    }
    @Test
    public void testCurrentBalance() {
        // Подготовка макета UserRepository
        String login = "andrey";
        User user = new User(1, "Andrey", login, "password123");
        user.setBalance(1000.0); // Устанавливаем баланс пользователя
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        double balance = userService.currentBalance(login);

        // Проверка результата
        assertEquals(1000.0, balance, 0.001); // Проверяем, что баланс вернулся корректно

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addLogEntry(user, UserActions.GET_BALANCE);
    }

    @Test
    public void testWithdrawalOfFundsSufficientBalance() {
        // Подготовка макета UserRepository
        String login = "art234";
        User user = new User(1, "Artur", login, "8765r4e3");
        user.setBalance(1000.0); // Устанавливаем баланс пользователя
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        assertDoesNotThrow(() -> userService.withdrawalOfFunds(login, 500.0));

        // Проверка результата
        assertEquals(500.0, user.getBalance(), 0.001); // Проверяем, что баланс уменьшился

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addLogEntry(user, UserActions.DEBIT);
        verify(userRepository, times(1)).addTransaction(user, TransactionType.DEBIT, 500.0);
    }

    @Test
    public void testBalanceReplenishment() {
        // Подготовка макета UserRepository
        String login = "rost12";
        User user = new User(1, "Rostislav", login, "8765");
        user.setBalance(500.0); // Устанавливаем баланс пользователя
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        userService.balanceReplenishment(login, 300.0);

        // Проверка результата
        assertEquals(800.0, user.getBalance(), 0.001); // Проверяем, что баланс увеличился на 300.0

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).addLogEntry(user, UserActions.DEBIT);
        verify(userRepository, times(1)).addTransaction(user, TransactionType.DEBIT, 300.0);
    }
    @Test
    public void testReplenishmentHistory() {
        String login = "katy4343";
        User user = new User(1, "Kate", login, "8765");
        when(userRepository.getUser(login)).thenReturn(user);

        // Вызов метода для тестирования
        userService.replenishmentHistory(login);

        // Проверка вызова метода в UserRepository
        verify(userRepository, times(1)).getTransaction(user);
    }
    @Test
    public void testAuditOfActions() {
        // Подготовка макета UserRepository
        LogEntry logEntry1 = new LogEntry(1, UserActions.DEBIT, LocalDateTime.now(), new User(1, "Viktoriya", "vikusya", "pass1"));
        LogEntry logEntry2 = new LogEntry(2, UserActions.AUTHORIZATION, LocalDateTime.now(), new User(2, "Max", "lineAlex", "pass2"));
        List<LogEntry> logEntries = new ArrayList<>();
        logEntries.add(logEntry1);
        logEntries.add(logEntry2);
        when(userRepository.getListActions()).thenReturn(logEntries);

        // Вызов метода для тестирования
        userService.auditOfActions();

        // Проверка вызовов методов в UserRepository
        verify(userRepository, times(1)).getListActions();
    }
}

