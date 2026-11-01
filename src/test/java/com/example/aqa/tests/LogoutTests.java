package com.example.aqa.tests;

import com.example.aqa.config.BaseTest;
import com.example.aqa.mocks.ExternalServiceMocks;
import com.example.aqa.steps.EndpointSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Feature("LOGOUT — завершение сессии")
class LogoutTests extends BaseTest {

    private final EndpointSteps steps = new EndpointSteps();

    @Test
    @Story("LOGOUT после LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("После LOGOUT токен удалён — ACTION возвращает 403")
    void logoutRemovesToken() {
        ExternalServiceMocks.authSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        steps.logout(token).then().statusCode(200);
        steps.doAction(token).then().statusCode(403);
    }

    @Test
    @Story("LOGOUT без LOGIN")
    @DisplayName("LOGOUT для неизвестного токена не ломает приложение (идемпотентно)")
    void logoutWithoutLogin() {
        steps.logout(validToken()).then().statusCode(200);
    }

    @Test
    @Story("Повторный LOGOUT")
    @DisplayName("Повторный LOGOUT безопасен")
    void doubleLogout() {
        ExternalServiceMocks.authSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        steps.logout(token).then().statusCode(200);
        steps.logout(token).then().statusCode(200);
    }
}