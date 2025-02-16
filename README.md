# Тестовое задание
Напишите приложение, которое по REST принимает запрос вида
**POST api/v1/wallet**
``` 
{
valletId: UUID,
operationType: DEPOSIT or WITHDRAW,
amount: 1000
}
```
После выполнять логику по изменению счета в базе данных. Также есть возможность получить баланс кошелька. 
**GET api/v1/wallets/{WALLET_UUID}**

Должны быть написаны миграции для базы данных с помощью liquibase.
Обратите особое внимание проблемам при работе в конкурентной среде (1000 RPS по одному кошельку).
Ни один запрос не должен быть не обработан (50Х error).
Предусмотрите соблюдение формата ответа для заведомо неверных запросов, когда кошелька не существует, не валидный json, или недостаточно средств.
Приложение должно запускаться в докер контейнере, база данных тоже.
Вся система должна подниматься с помощью docker-compose.
Предусмотрите возможность настраивать различные параметры приложения и базы данных без пересборки контейнеров.
Эндпоинты должны быть покрыты тестами.

### Cтек: 
* **java 8-17**
* **Spring 3**
* **Postgresql**

# Подготовка

### Запуск docker-compose:
- **измените данные в docker-compose.yml**
``` 
services:
  recipe:
      container_name: wallet-rest-api
      build:
        context: .
        dockerfile: Dockerfile
      ports:
        - '8080:8080'
      environment:
          SPRING_DATASOURCE_URL: jdbc:postgresql://db_pg:5432/wallets
          SPRING_DATASOURCE_USERNAME: ИМЯ ПОЛЬЗОВАТЕЛЯ
          SPRING_DATASOURCE_PASSWORD: ПАРОЛЬ
      depends_on:
        - db_pg

  db_pg:
      image: postgres
      environment:
        POSTGRES_DB: wallets
        POSTGRES_USER: ИМЯ ПОЛЬЗОВАТЕЛЯ
        POSTGRES_PASSWORD: ПАРОЛЬ
      ports:
        - '5432:5432'
      volumes:
        - ./init.sql:/docker-entrypoint-initdb.d/init.sql
```
- **выполните команду**
``` 
docker-compose up
```
### При запуске d базу дынных добавляются таблица и два кошелька.
**Файл init.sql:**
```
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS wallets (
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
balance DECIMAL(10, 2),
version BIGINT
);

INSERT INTO wallets (balance, version) VALUES (100.00, 1), (250.50, 1);
```

### Запуск в Intellij IDEA:
- **настройте и проверте соединение с БД**
- **измените данные в application.properties**

``` 
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/wallets
spring.datasource.username=ИМЯ ПОЛЬЗОВАТЕЛЯ
spring.datasource.password=ПАРОЛЬ
```

