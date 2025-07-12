math-sequences-api — Инструкция по локальному запуску
📋 Предусловия
Убедитесь, что у вас установлены:

Java 21+

Maven 3.6+

PostgreSQL

Postman (или другой API-клиент для тестирования)

⚙️ Настройка базы данных:

Создайте базу данных:
sql
CREATE DATABASE math_sequences;

Убедитесь, что в application.properties указаны корректные параметры подключения к базе данных:
properties
spring.datasource.url=jdbc:postgresql://localhost:5432/math_sequences
spring.datasource.username
spring.datasource.password

🛠 Liquibase
Liquibase автоматически применит миграции при запуске, если они находятся в classpath:/db/changelog/generated-changelog.xml.

Убедитесь, что файл changelog содержит таблицы для пользователей, ролей, карт и их связей.

👥 Создание аккаунтов
После запуска приложения необходимо создать пользователя USER.

Пример запросов (через Postman или curl):

1. Создать пользователя с ролью USER

POST /register
Content-Type: application/json

{
    "username": "user1",
    "password": "password123",
    "confirmPassword": "password123",
    "email": "user1@example.com"
}

🔐 Аутентификация
После регистрации вы можете получить JWT токен через эндпоинт логина, например:

POST /auth
Content-Type: application/json

{
  "username": "user1",
  "password": "password123"
}
Ответ будет содержать токен доступа, который нужно использовать для авторизации в дальнейших запросах.

## API Документация

Чтобы просмотреть все доступные методы API:

1. Запустите приложение
2. И перейдите на http://localhost:8080/swagger-ui/index.html