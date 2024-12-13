Spring Boot CRUD приложение для загрузки файлов

Данное Spring Boot приложение предоставляет REST API для выполнения следующих функций:
CRUD операции:
- Аутентификация:
    Эндпоинты для входа (login) и выхода (logout) пользователей.
- Загрузка и управление файлами, отправленными через multipart/form-data на REST эндпоинты.
- Сохранение названия и содержимого файлов в базе данных.

Технологии:
    Spring Boot
    Spring Security
    Spring Data JPA с Hibernate
    Docker/Docker Compose для контейнеризации

Архитектура
Приложение написано в соответствии с стандартом MVC, со слоями Controller-Service-Repository

1. Контроллер:
    Обрабатывает запросы и формирует ответы:
        Эндпоинты для входа и выхода пользователей.
        Эндпоинты CRUD для управления файлами.
2. Cервис:
    Управляет аутентификацией и сессиями пользователей.
    Обрабатывает загружаемые файлы в формате multipart/form-data.
3. Репозиторий:
    Осуществляет взаимодействие с базой данных с помощью Spring Data JPA:
        Управляет пользовательскими данными для аутентификации.
        Сохраняет названия и содержимое файлов.

Эндпоинты
    Эндпоинты аутентификации
        POST /login
            Аутентификация пользователя с использованием username и password.
            Возвращает JWT токен для последующих запросов.
        POST /logout
            Завершает текущую сессию и деактивирует токен.

    Эндпоинты для работы с файлами
        POST /file
            Загрузка файла.
            Сохраняет названия и содержимое файла в базе данных.
        GET /file
            Получение файла и скачивание содержимого по названию файла.
        PUT /file
            Редактирование существующего файла.
        DELETE /file
            Удаление файла по названию.
        GET /list
            Получение списка информации о всех сохранённых файлах в формате json.

Предустановленные пользователи
    Три предустановленных пользователя доступны для тестирования:
        Логин: user
        Пароль: password
        Логин: user2
        Пароль: password2
        Логин: user3
        Пароль: password3

Запуск и настройка
1. Запуск как JAR
    java -jar out/artifacts/CloudStorage/CloudStorage.jar
    Приложение будет доступно по адресу http://localhost:8080.
2. Запуск с помощью Docker Compose
    Постройте Docker-image:
        docker build -t cloudimage:latest .
    Запустите приложение через Docker Compose:
        docker-compose up
    Приложение будет доступно по адресу http://localhost:8080.

По умолчанию приложение использует базу данных MySQL.
Чтобы подключить другую базу данных, измените настройки в файле application.yml

Безопасность
    Приложение использует Spring Security для аутентификации и авторизации.
    Для безопасной передачи данных используется JWT-token.