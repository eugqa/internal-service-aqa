package com.example.aqa.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;

/**
 * Базовый класс для всех тестов.
 * - Поднимает/останавливает WireMock на порту 8888 (должен совпадать с -Dmock у приложения).
 * - Сбрасывает заглушки и состояние RestAssured перед каждым тестом.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected static WireMockServer wireMock;

    @BeforeAll
    void startWireMock() {
        TestConfig.initRestAssured();
        wireMock = new WireMockServer(WireMockConfiguration.options().port(TestConfig.MOCK_PORT));
        wireMock.start();
    }

    @AfterAll
    void stopWireMock() {
        if (wireMock != null) {
            wireMock.stop();
            wireMock = null;
        }
    }

    @BeforeEach
    void resetMocks() {
        wireMock.resetAll();
        // пересобрать конфиг RestAssured на случай переопределения base.url в CLI
        TestConfig.initRestAssured();
    }

    // ---------- фабрики запросов ----------

    protected RequestSpecification validRequest() {
        return given()
                .header("X-Api-Key", TestConfig.API_KEY)
                .contentType(TestConfig.FORM)
                .accept("application/json");
    }

    protected RequestSpecification requestWithApiKey(String apiKey) {
        return given()
                .header("X-Api-Key", apiKey)
                .contentType(TestConfig.FORM)
                .accept("application/json");
    }

    protected RequestSpecification requestWithoutApiKey() {
        return given()
                .contentType(TestConfig.FORM)
                .accept("application/json");
    }

    /** Валидный токен: 32 символа A-Z0-9. */
    protected static String validToken() {
        return "ABCDEF1234567890ABCDEF1234567890";
    }

    @Attachment(value = "Тело ответа", type = "application/json")
    protected String attachBody(String body) {
        return body;
    }
}