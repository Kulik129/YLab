package io.ylab.infrastructure.in;

import io.ylab.domain.action.UserActions;
import io.ylab.domain.models.LogEntry;
import io.ylab.domain.models.Transaction;
import io.ylab.domain.models.User;
import io.ylab.domain.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для обработки входящего потока данных.
 * Ввод пользовательских данных.
 */

public class InputDataService {


    /**
     * Вход в приложение.
     */
    public static void login(UserService service) {
        try {
            Scanner scannerLogin = new Scanner(System.in);

            System.out.println("Введите логин");
            String login = scannerLogin.nextLine();

            if (login != null && service.userMap.containsKey(login)) {
                System.out.println("Введите пароль");
                String password = scannerLogin.nextLine();
                User user = service.userMap.get(login);
                if (password != null && user.getPassword().equals(password)) {
                    service.authorizationUser(login, password);
                    if (login.equals("admin")) {
                        adminPanel(service, login);
                    } else {
                        navigationMenu(service, login);
                    }
                } else {
                    System.out.println("Ошибка в пароле..");
                    login(service);
                    service.auditOfActions(user, UserActions.FATAL);
                }
            } else {
                System.out.println("Логина: " + login + " не существует");
                login(service);
            }
            scannerLogin.close();
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }

    /**
     * Панель администратора.
     *
     * @param service сервис обработки основных методов.
     * @param login   логин пользователя.
     */
    public static void adminPanel(UserService service, String login) {
        try {
            User user = service.userMap.get(login);
            Scanner scannerAdmin = new Scanner(System.in);
            System.out.println(
                    "0 Посмотреть аудит действий" + "\n" +
                            "5 Выйти из приложения" + "\n"
            );
            String answer = scannerAdmin.nextLine();

            switch (answer) {
                case "0":
                    auditOUserActions(service);
                    adminPanel(service, login);
                    break;
                case "5":
                    logOut(service, user);
                    break;
                default:
                    System.out.println("Такой команды нет...");
                    adminPanel(service, login);
            }
        } catch (Exception ex) {
            System.err.println("Произошла ошибка: " + ex.getMessage());
            adminPanel(service,login);
        }
    }

    /**
     * Просмотр действий пользователя.
     *
     * @param service сервис обработки основных методов.
     */
    public static void auditOUserActions(UserService service) {
        try {
            if (service.listActions.isEmpty()) {
                System.out.println("Активностей нет");
            } else {
                for (LogEntry log : service.listActions) {
                    System.out.println(log);
                }
            }
        }catch (Exception ex){
            System.err.println("Произошла ошибка: " + ex.getMessage());
            auditOUserActions(service);
        }
    }

    /**
     * Просмотр истории операций.
     *
     * @param service сервис обработки основных методов.
     * @param login   логин пользователя по которому будет осуществляться поиск его операций.
     */
    public static void viewingHistory(UserService service, String login) {
        try {
            User user = service.userMap.get(login);
            service.auditOfActions(user, UserActions.HISTORY);
            List<Transaction> userTransactions = new ArrayList<>();

            for (Transaction transaction : service.transactions) {
                if (transaction.getUser().getLogin().equals(login)) {
                    userTransactions.add(transaction);
                }
            }
            if (userTransactions.isEmpty()) {
                System.out.println("История пуста...");
            } else {
                for (Transaction transaction : userTransactions) {
                    System.out.println(transaction);
                }
            }
        } catch (Exception ex) {
            System.err.println("Произошла ошибка: " + ex.getMessage());
            viewingHistory(service, login);
        }
    }

    /**
     * Навигационное меню.
     *
     * @param service сервис обработки основных методов.
     * @param login   логин пользователя, по которому будет подбираться меню.
     */
    public static void navigationMenu(UserService service, String login) {
        try {
            System.out.println(
                    "1 Посмотреть баланс" + "\n" +
                            "2 Пополнить баланс" + "\n" +
                            "3 снятие наличных" + "\n" +
                            "4 история операций" + "\n" +
                            "5 выход из приложения"
            );
            if (login.equals("admin")) {
                adminPanel(service, login);
            }
            Scanner scannerMenu = new Scanner(System.in);
            String answer = scannerMenu.nextLine();
            User user = service.userMap.get(login);
            switch (answer) {
                case "1":
                    System.out.println("Ваш баланс: " + service.currentBalance(user) + "\n");
                    navigationMenu(service, login);
                    break;
                case "2":
                    System.out.println("Введите сумму для пополнения");
                    double sumCredit = scannerMenu.nextDouble();
                    service.balanceReplenishment(login, sumCredit);
                    navigationMenu(service, login);
                    break;
                case "3":
                    System.out.println("Введите сумму для снятия");
                    double sumDebit = scannerMenu.nextDouble();
                    service.withdrawalOfFunds(login, sumDebit);
                    navigationMenu(service, login);
                    break;
                case "4":
                    viewingHistory(service, login);
                    navigationMenu(service, login);
                    break;
                case "5":
                    logOut(service, user);
                    break;
                default:
                    System.out.println("Вы ошиблись! Такой команды нет...");
                    navigationMenu(service, login);
            }
        }catch (Exception ex){
            System.err.println("Произошла ошибка: " + ex.getMessage());
            navigationMenu(service, login);
        }
    }

    /**
     * Выход из приложения.
     *
     * @param service сервис обработки основных методов.
     * @param user    пользователь приложения.
     */
    public static void logOut(UserService service, User user) {
        try {
            service.auditOfActions(user, UserActions.LOGOUT);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Всего хорошего!");
            System.out.println("1 войти" + "\n" + "2 Регистрация");
            String answer = scanner.nextLine();
            switch (answer) {
                case "1":
                    login(service);
                    break;
                case "2":
                    registration(service);
                    break;
                default:
            }
        }catch (Exception ex){
            System.err.println("Произошла ошибка: " + ex.getMessage());
            logOut(service, user);
        }
    }

    /**
     * Регистрация в приложении.
     *
     * @param service сервис обработки основных методов
     */
    public static void registration(UserService service) {
        try {
            Scanner scannerRegistration = new Scanner(System.in);
            String name;
            String loginNew;
            String passwordNew;

            do {
                System.out.println("Введите имя");
                name = scannerRegistration.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Имя не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите имя.");
                }
            } while (name.isEmpty());

            do {
                System.out.println("Введите логин");
                loginNew = scannerRegistration.nextLine().trim();
                if (loginNew.isEmpty()) {
                    System.out.println("Логин не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите логин.");
                }
            } while (loginNew.isEmpty() || service.userMap.containsKey(loginNew));

            do {
                System.out.println("Введите пароль");
                passwordNew = scannerRegistration.nextLine().trim();
                if (passwordNew.isEmpty()) {
                    System.out.println("Пароль не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите пароль.");
                }
            } while (passwordNew.isEmpty());

            service.registrationUser(name, loginNew, passwordNew);
            navigationMenu(service, loginNew);
            scannerRegistration.close();
        } catch (Exception ex) {
            System.err.println("Произошла ошибка: " + ex.getMessage());
            registration(service);
        }
    }

    /**
     * Начальное меню.
     * Предлагает пользователю войти или зарегистрироваться.
     */
    public void start() {
        try {
            UserService inputService = new UserService();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Нажмите 1 для входа" + "\n" + "Нажмите 2 для регистрации");
            String answer = scanner.nextLine();

            switch (answer) {
                case "1":
                    InputDataService.login(inputService);
                    break;
                case "2":
                    InputDataService.registration(inputService);
                    break;
                default:
                    System.out.println("Такой команды нет... ");
                    start();
            }
        }catch (Exception ex) {
            System.err.println("Произошла ошибка: " + ex.getMessage());
            start();
        }
    }
}
