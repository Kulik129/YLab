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


//        inputDataService.registration();

//        inputDataService.login();

        inputDataService.start();













//        InputDataService inputDataService = new InputDataService(new UserService(new UserRepository()));
//        inputDataService.registration();



//        InputDataService inputDataService1 = new InputDataService();


//
//        UserRepository repository = new UserRepository();
//        // Add user in map
//        User user = new User(repository.getUserMap().size() + 1, "name", "login", "password");
//        repository.addUserInMap(user);
////        user.setTransactions(repository.addTransaction(user, TransactionType.CREDIT,200));
//
////        System.out.println(repository.userMap.get("login"));
//
//        // Get user
//        System.out.println(repository.getUser("admin"));
//        System.out.println(repository.getUser("vik"));
//        System.out.println(repository.getUser("login"));
//
//        // Проверка на наличие пользователя в мапе
//        System.out.println(repository.userPresence("vik"));
//
//// ---------------------------------------------------------------------------------------------------------------------
////        // Add transactions user
//
//        List<Transaction> list = new ArrayList<>();
//        list.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//        list.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//        list.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//        list.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//        list.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//
//        user.setTransactions(list);
//
//        for (Transaction transaction : user.getTransactions()) {
//            System.out.println(transaction);
//        }
//
//
//        List<Transaction> list2 = new ArrayList<>();
//        User admin = repository.getUser("admin");
//        list2.add(new Transaction(1, TransactionType.CREDIT, 67, LocalDateTime.now()));
//        list2.add(new Transaction(1, TransactionType.CREDIT, 456, LocalDateTime.now()));
//        admin.setTransactions(list2);
//
//        for (Transaction transaction1 : admin.getTransactions()) {
//            System.out.println(transaction1);
//        }
//        System.out.println();
//        System.out.println();
//        System.out.println();
//
//        //Add transaction
//        User vik = repository.getUser("vik");
//        repository.addTransaction(vik, TransactionType.DEBIT, 4000);
//        repository.addTransaction(vik, TransactionType.DEBIT, 650);
//        repository.addTransaction(vik, TransactionType.DEBIT, 7600);
//        //get transaction
//        repository.getTransaction(vik);
//
//        // ---------------------------------------------------------------------------------------------------------------------
//        // Add log

    }


}