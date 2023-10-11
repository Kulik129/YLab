package io.ylab.application;

import io.ylab.domain.service.UserService;
import io.ylab.infrastructure.in.InputDataService;
import io.ylab.infrastructure.repository.UserRepository;

/**
 * Точка входа в приложение.
 */

public class Main {
    public static void main(String[] args) {
        // Создаем репозиторий пользователей
        UserRepository userRepository = new UserRepository();
        // Создаем сервис пользователей, внедряя в него репозиторий
        UserService userService = new UserService(userRepository);
        // Создаем сервис ввода данных, внедряя в него сервис пользователей
        InputDataService inputDataService = new InputDataService(userService);

        inputDataService.start();
    }
}