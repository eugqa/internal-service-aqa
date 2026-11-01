package com.example.aqa.tests;

import com.example.aqa.config.BaseTest;
import com.example.aqa.mocks.ExternalServiceMocks;
import com.example.aqa.steps.EndpointSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@Epic("Тестируемое приложение")
@Feature("Сквозные пользовательские сценарии")
class WorkflowTests extends BaseTest {

    private final EndpointSteps steps = new EndpointSteps();

    @Test
    @Story("Полный жизненный цикл сессии")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("LOGIN → ACTION → LOGOUT → ACTION(403)")
    void fullLifecycle() {
        ExternalServiceMocks.authSuccess();
        ExternalServiceMocks.doActionSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        steps.doAction(token).then().statusCode(200);
        steps.logout(token).then().statusCode(200);
        steps.doAction(token).then().statusCode(403);
    }

    @Test
    @Story("Повторная аутентификация")
    @DisplayName("Повторный LOGIN для того же токена не ломает сервис")
    void reLoginSameToken() {
        ExternalServiceMocks.authSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        // контракт не оговаривает поведение — фиксируем устойчивость
        steps.login(token).then().statusCode(anyOf(equalTo(200), equalTo(409)));
    }

    @Test
    @Story("Независимые пользователи")
    @DisplayName("LOGOUT одного токена не влияет на другие")
    void independentTokens() {
        ExternalServiceMocks.authSuccess();
        ExternalServiceMocks.doActionSuccess();

        String tokenA = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String tokenB = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

        steps.login(tokenA).then().statusCode(200);
        steps.login(tokenB).then().statusCode(200);
        steps.logout(tokenA).then().statusCode(200);

        steps.doAction(tokenA).then().statusCode(403);
        steps.doAction(tokenB).then().statusCode(200);
    }
}