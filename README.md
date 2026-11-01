# internal-service-aqa

Автотесты для Spring Boot-сервиса `internal` (тестовое задание AQA).

## Стек
- Java 17
- JUnit 5
- RestAssured (HTTP-клиент)
- WireMock (мок внешнего `/auth` и `/doAction`)
- Allure (отчёты)
- Gradle

## Что покрыто
- Валидация `X-Api-Key`, формата `token`, `action`, обязательных параметров
- LOGIN: успех / ошибки внешнего сервиса / сохранение токена
- ACTION: запрет без LOGIN / успех / ошибки внешнего сервиса
- LOGOUT: удаление токена / идемпотентность / повторный logout
- Сквозные пользовательские сценарии

## Как запустить

### 1. Запустить тестируемое приложение (в отдельном терминале)
```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888/ internal-0.0.1-SNAPSHOT.jar