package com.example.aqa.config;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

/**
 * Глобальная конфигурация тестов.
 * Значения можно переопределить через -Dbase.url=... и -Dmock.url=...
 */
public final class TestConfig {

    private TestConfig() {}

    public static final String API_KEY  = "qazWSXedc";
    public static final String ENDPOINT = "/endpoint";

    public static final String BASE_URL = System.getProperty("base.url", "http://localhost:8080");
    public static final String MOCK_URL = System.getProperty("mock.url", "http://localhost:8888");

    /** Порт мока. Извлекается из MOCK_URL, но должен совпадать с конфигом запуска приложения. */
    public static final int MOCK_PORT = Integer.parseInt(
            MOCK_URL.substring(MOCK_URL.lastIndexOf(':') + 1).replaceAll("/.*", "")
    );

    /** Пресеты Content-Type/Accept для эндпоинта. */
    public static final ContentType FORM = ContentType.URLENC;

    public static void initRestAssured() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5_000)
                        .setParam("http.socket.timeout", 5_000));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}