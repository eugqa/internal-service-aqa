package com.example.aqa.tests;

import com.example.aqa.config.BaseTest;
import com.example.aqa.mocks.ExternalServiceMocks;
import com.example.aqa.steps.EndpointSteps;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Feature("ACTION — действие пользователя")
class ActionTests extends BaseTest {

    private final EndpointSteps steps = new EndpointSteps();

    @Test
    @Story("ACTION без LOGIN")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("ACTION отклоняется с 403, если LOGIN не выполнялся")
    void actionWithoutLogin() {
        ExternalServiceMocks.doActionSuccess();

        Response response = steps.doAction(validToken());

        response.then().statusCode(403);
        // /doAction на внешний сервис НЕ должен уходить
        verify(0, postRequestedFor(urlEqualTo("/doAction")));
    }

    @Test
    @Story("ACTION после LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("ACTION успешен после LOGIN, если /doAction отвечает 200")
    void actionAfterLoginSuccess() {
        ExternalServiceMocks.authSuccess();
        ExternalServiceMocks.doActionSuccess();
        String token = validToken();

        steps.login(token).then().statusCode(200);
        Response response = steps.doAction(token);

        response.then().statusCode(200);
        verify(postRequestedFor(urlEqualTo("/doAction"))
                .withRequestBody(containing("token=" + token)));
    }

    @Test
    @Story("ACTION после LOGIN")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("ACTION возвращает 5xx, если /doAction отвечает 500")
    void actionExternalFailure() {
        ExternalServiceMocks.authSuccess();
        ExternalServiceMocks.doActionFailure(500);
        String token = validToken();

        steps.login(token).then().statusCode(200);
        Response response = steps.doAction(token);

        response.then().statusCode(500);
    }
}