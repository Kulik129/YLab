# _**Wallet-Service**_

#### Консольное приложение

---


## Запуск Приложения

Для запуска этого приложения, выполните следующие шаги:

1. Убедитесь, что у вас установлена Java версии 17.
2. Запустите докер контейнер с помощью команды `docker-compode up -d ` перейдя в директорию `src/main/java/io/ylab/container`
3. Затем привычным для Вас способом запустите класс Main, перейдя в директорию `src/main/java/io/ylab/application/Main.java`
4. Следуйте инструкциям на экране, чтобы войти, зарегистрироваться и выполнять различные операции.

---

### _Для входа в приложение под учетной записью:_
* администратора - введите логин: **_admin_** и пароль **_1234_**.
* тестового пользователя - введите логин **_vik_** и пароль **_4321_**.

---

### Структура Проекта:
* Пройдя по пути: `src/main/java/io/ylab` вы откроете пакеты содержащие необходимый код,
  разделенный по разным пакетам, которые содержат классы выполняющие свои задачи.
* Пройдя по пути: `src/test/java/domain/service` вы откроете тесты которыми покрыт сервис приложения.
* Пройдя по пути: `src/test/java/io/ylab/infrastructure/repository/` вы откроете тесты которыми покрыт репозиторий приложения.

--- 

### Советы

* Помните, что это консольное приложение, поэтому вы будете взаимодействовать с ним через командную строку.
* Убедитесь, что вы вводите корректные данные, чтобы избежать ошибок.
* P.S. Но на всякий случай они обрабатываются и будут выводить вам подсказки.

---
