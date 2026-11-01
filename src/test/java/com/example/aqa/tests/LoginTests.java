package com.example.aqa.tests;

import com.example.aqa.config.BaseTest;
import com.example.aqa.mocks.ExternalServiceMocks;
import com.example.aqa.steps.EndpointSteps;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

@Feature("LOGIN — аутентификация токена")
class LoginTests extends BaseTest {

    private final EndpointSteps steps = new EndpointSteps();

    @Test
    @Story("Успешный LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("LOGIN успешен, если /auth отвечает 200")
    void loginSuccess() {
        ExternalServiceMocks.authSuccess();
        String token = validToken();

        Response response = steps.login(token);

        response.then().statusCode(200);
        verify(postRequestedFor(urlEqualTo("/auth"))
                .withRequestBody(containsString("token=" + token)));
    }

    @Test
    @Story("Ошибка внешнего сервиса")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("LOGIN возвращает 5xx, если /auth отвечает 500")
    void loginExternalServerError() {
        ExternalServiceMocks.authFailure(500);

        Response response = steps.login(validToken());

        response.then().statusCode(allOf(greaterThanOrEqualTo(500), lessThan(600)));
    }

    @Test
    @Story("Ошибка внешнего сервиса")
    @DisplayName("LOGIN возвращает 4xx/5xx, если /auth отвечает 401")
    void loginExternalUnauthorized() {
        ExternalServiceMocks.authFailure(401);

        Response response = steps.login(validToken());

        // контракт не уточняет — фиксируем факт, что успешного ответа нет
        response.then().statusCode(not(200));
    }

    @Test
    @Story("Сохранение токена")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("После успешного LOGIN токен сохранён (ACTION проходит)")
    void tokenPersistedAfterLogin() {
        ExternalServiceMocks.authSuccess();
        ExternalServiceMocks.doActionSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        steps.doAction(token).then().statusCode(200);
    }
}