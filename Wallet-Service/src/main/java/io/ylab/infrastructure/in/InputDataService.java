package io.ylab.infrastructure.in;

import io.ylab.domain.service.UserServiceImpl;

import java.util.Scanner;

/**
 * Класс для обработки входящего потока данных.
 * Ввод пользовательских данных.
 */

public class InputDataService {
    private UserServiceImpl service;

    public InputDataService(UserServiceImpl userServiceImpl) {
        this.service = userServiceImpl;
    }

    /**
     * Начальное меню.
     * Предлагает пользователю войти или зарегистрироваться.
     */
    public void start() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1 - Вход" + "\n" + "2 - Регистрация");

            String answer = scanner.nextLine();
            switch (answer) {
                case "1":
                    login();
                    break;
                case "2":
                    registration();
                    break;
                default:
                    System.out.println("Ошибка команды.." + "\n");
                    start();
            }
        } catch (Exception ex) {
            System.out.println("Произошла ошибка ввода...");
            start();
        }
    }

    /**
     * Навигационное меню с доступными командами для пользователя.
     *
     * @param login логин пользователя.
     */
    public void navigationMenu(String login) {
        try {
            if (login.equals("admin")) {
                adminPanel();
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println(
                        "1 - Текущий баланс" + "\n" +
                                "2 - Снятие наличных" + "\n" +
                                "3 - Пополнение баланса" + "\n" +
                                "4 - История операций" + "\n" +
                                "5 - Выход из приложения"
                );
                String answer = scanner.nextLine();
                switch (answer) {
                    case "1":
                        System.out.println("Ваш баланс: " + service.currentBalance(login) + "\n");
                        navigationMenu(login);
                        break;
                    case "2":
                        System.out.println("Введите сумму для снятия: ");
                        double sumDebit = scanner.nextDouble();
                        service.withdrawalOfFunds(login, sumDebit);
                        navigationMenu(login);
                        break;
                    case "3":
                        System.out.println("Введите сумму для пополнения: ");
                        double sumCredit = scanner.nextDouble();
                        service.balanceReplenishment(login, sumCredit);
                        navigationMenu(login);
                        break;
                    case "4":
                        service.replenishmentHistory(login);
                        navigationMenu(login);
                        break;
                    case "5":
                        System.out.println("Всего хорошего!" + "\n");
                        login();
                        break;
                    default:
                        System.out.println("Ошибка команды.." + "\n");
                        navigationMenu(login);
                }
            }
        } catch (Exception ex) {
            System.out.println("Произошла ошибка ввода...");
            navigationMenu(login);
        }
    }

    /**
     * Регистрация пользователя в приложении.
     */
    public void registration() {
        try {
            Scanner scanner = new Scanner(System.in);

            String name;
            String login;
            String password;

            do {
                System.out.println("Придумайте логин");
                login = scanner.nextLine().trim();
                if (login.isEmpty()) {
                    System.out.println("Логин не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите логин.");
                }
            } while (login.isEmpty());
            do {
                System.out.println("Придумайте пароль");
                password = scanner.nextLine().trim();
                if (password.isEmpty()) {
                    System.out.println("Логин не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите логин.");
                }
            } while (password.isEmpty());
            do {
                System.out.println("Введите имя");
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Имя не может быть пустым или состоять только из пробелов."
                            + "\n" + "Пожалуйста, введите имя.");
                }
            } while (name.isEmpty());
            if (service.registrationUser(login, password, name)) {
                navigationMenu(login);
            } else {
                registration();
            }
        } catch (Exception ex) {
            System.err.println("Произошла ошибка: " + ex.getMessage());
            registration();
        }
    }

    /**
     * Вход в приложение.
     */
    public void login() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Ведите логин");
            String login = scanner.nextLine();
            System.out.println("Ведите пароль");
            String password = scanner.nextLine();

            if (service.authorizationUser(login, password)) {
                navigationMenu(login);
            } else {
                System.out.println("Ошибка авторизации");
                start();
            }
        } catch (Exception ex) {
            System.out.println("Произошла ошибка ввода...");
            login();
        }
    }

    /**
     * Панель администратора.
     */
    public void adminPanel() {
        try {
            System.out.println(
                    "0 - Аудит действий пользователя" + "\n" +
                            "5 - Выход из приложения" + "\n"
            );
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            switch (answer) {
                case "0":
                    service.auditOfActions();
                    break;
                case "5":
                    login();
                    break;
                default:
            }
            adminPanel();
        } catch (Exception ex) {
            System.out.println("Произошла ошибка ввода...");
            adminPanel();
        }
    }
}
