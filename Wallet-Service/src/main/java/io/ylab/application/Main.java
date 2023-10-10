package io.ylab.application;

import io.ylab.infrastructure.in.InputDataService;

/**
 * Точка входа в приложение.
 */

public class Main {
    public static void main(String[] args) {
        InputDataService inputDataService = new InputDataService();
        inputDataService.start();
    }
}