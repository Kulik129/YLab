package domain.service;


import io.ylab.domain.action.TransactionType;
import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.User;
import io.ylab.domain.service.UserServiceImpl;
import io.ylab.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrationUser() {
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(1, name,login,password);
        Mockito.when(userRepository.userPresence(login)).thenReturn(false);

        assertTrue(userServiceImpl.registrationUser(login, password, name));

        Mockito.verify(userRepository).saveUser(user);
        Mockito.verify(userRepository).saveAction(user, UserActions.REGISTRATION);
    }

    @Test
    void testAuthorizationUser(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(name,login,password);

        Mockito.when(userRepository.userPresence(login)).thenReturn(true);
        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);

        assertTrue(userServiceImpl.authorizationUser(login,password));

        Mockito.verify(userRepository).saveAction(user, UserActions.AUTHORIZATION);
    }

    @Test
    void testAuthorizationUserInvalidPassword(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";
        User user = new User(name, login, "3456789");

        Mockito.when(userRepository.userPresence(login)).thenReturn(true);
        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);

        assertFalse(userServiceImpl.authorizationUser(login, password));

        Mockito.verify(userRepository).saveAction(user, UserActions.FATAL);
    }

    @Test
    void testCurrentBalance(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(name,login,password);
        user.setBalance(100.0);

        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);
        Mockito.when(userRepository.userPresence(login)).thenReturn(true);

        userServiceImpl.withdrawalOfFunds(login, 50.0);

        assertEquals(50.0, user.getBalance());
        Mockito.verify(userRepository).updateBalance(user);
        Mockito.verify(userRepository).saveAction(user, UserActions.DEBIT);
        Mockito.verify(userRepository).saveTransaction(user, TransactionType.DEBIT, 50.0);
    }

    @Test
    void testWithdrawalOfFundsInsufficientBalance(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(name,login,password);
        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);
        Mockito.when(userRepository.userPresence(login)).thenReturn(true);
    }

    @Test
    void testBalanceReplenishment(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(name,login,password);
        user.setBalance(100.0);

        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);

        userServiceImpl.balanceReplenishment(login, 50.0);

        assertEquals(150.0, user.getBalance());

        Mockito.verify(userRepository).updateBalance(user);
        Mockito.verify(userRepository).saveAction(user, UserActions.CREDIT);
        Mockito.verify(userRepository).saveTransaction(user, TransactionType.CREDIT, 50.0);
    }

    @Test
    void testReplenishmentHistory(){
        String login = "tomas";
        String password = "1234";
        String name = "Tomas Shelbi";

        User user = new User(name,login,password);
        Mockito.when(userRepository.findByLogin(login)).thenReturn(user);

        userServiceImpl.replenishmentHistory(login);
        Mockito.verify(userRepository).getTransaction(user);
        Mockito.verify(userRepository).saveAction(user, UserActions.HISTORY);
    }
}

