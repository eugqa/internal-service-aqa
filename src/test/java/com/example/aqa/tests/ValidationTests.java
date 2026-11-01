package com.example.aqa.tests;

import com.example.aqa.config.BaseTest;
import com.example.aqa.config.TestConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("Тестируемое приложение")
@Feature("Валидация входящих запросов")
class ValidationTests extends BaseTest {

    @Test
    @Story("Проверка API-ключа")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Запрос без X-Api-Key отклоняется с 401")
    void testRequestWithoutApiKey() { // <-- ПЕРЕИМЕНОВАНО
        requestWithoutApiKey()
                .formParam("token", validToken())
                .formParam("action", "LOGIN")
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(401);
    }

    @Test
    @Story("Проверка API-ключа")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Запрос с неверным X-Api-Key отклоняется с 401")
    void testRequestWithWrongApiKey() { // <-- ПЕРЕИМЕНОВАНО
        requestWithApiKey("wrong-key-123")
                .formParam("token", validToken())
                .formParam("action", "LOGIN")
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(401);
    }

    @ParameterizedTest(name = "[{index}] token=\"{0}\"")
    @ValueSource(strings = {
            "short",                                              // слишком короткий
            "TOOLONGTOKEN_ABCDEF1234567890ABCDEF1234567890123",   // слишком длинный
            "abcdef1234567890abcdef1234567890",                   // lower-case
            "ABCDEF1234567890ABCDEF123456789!",                   // спецсимвол
            "ABCDEF1234567890ABCDEF123456789 ",                   // пробел
            "АБВГД1234567890ABCDEF1234567890"                     // кириллица
    })
    @Story("Проверка формата token")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Невалидный token отклоняется с 400")
    void invalidTokenFormat(String badToken) {
        validRequest()
                .formParam("token", badToken)
                .formParam("action", "LOGIN")
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Проверка action")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Неизвестный action отклоняется с 400")
    void unknownAction() {
        validRequest()
                .formParam("token", validToken())
                .formParam("action", "UNKNOWN")
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Обязательные параметры")
    @DisplayName("Запрос без token отклоняется с 400")
    void missingToken() {
        validRequest()
                .formParam("action", "LOGIN")
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(400);
    }

    @Test
    @Story("Обязательные параметры")
    @DisplayName("Запрос без action отклоняется с 400")
    void missingAction() {
        validRequest()
                .formParam("token", validToken())
                .when()
                .post(TestConfig.ENDPOINT)
                .then()
                .statusCode(400);
    }
}