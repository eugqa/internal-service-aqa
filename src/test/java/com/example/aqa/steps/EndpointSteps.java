package com.example.aqa.steps;

import com.example.aqa.config.TestConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EndpointSteps {

    @Step("LOGIN для токена {token}")
    public Response login(String token) {
        return given()
                .header("X-Api-Key", TestConfig.API_KEY)
                .contentType(TestConfig.FORM)
                .accept("application/json")
                .formParam("token", token)
                .formParam("action", "LOGIN")
                .when()
                .post(TestConfig.ENDPOINT);
    }

    @Step("ACTION для токена {token}")
    public Response doAction(String token) {
        return given()
                .header("X-Api-Key", TestConfig.API_KEY)
                .contentType(TestConfig.FORM)
                .accept("application/json")
                .formParam("token", token)
                .formParam("action", "ACTION")
                .when()
                .post(TestConfig.ENDPOINT);
    }

    @Step("LOGOUT для токена {token}")
    public Response logout(String token) {
        return given()
                .header("X-Api-Key", TestConfig.API_KEY)
                .contentType(TestConfig.FORM)
                .accept("application/json")
                .formParam("token", token)
                .formParam("action", "LOGOUT")
                .when()
                .post(TestConfig.ENDPOINT);
    }

    @Step("Запрос с произвольным action={action} и token={token}")
    public Response raw(String token, String action) {
        return given()
                .header("X-Api-Key", TestConfig.API_KEY)
                .contentType(TestConfig.FORM)
                .accept("application/json")
                .formParam("token", token)
                .formParam("action", action)
                .when()
                .post(TestConfig.ENDPOINT);
    }
}