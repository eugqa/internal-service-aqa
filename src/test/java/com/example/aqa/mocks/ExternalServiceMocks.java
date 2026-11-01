package com.example.aqa.mocks;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Готовые stub-наборы для внешнего сервиса.
 * Внешний сервис возвращает любое тело — приложение смотрит только на код.
 */
public final class ExternalServiceMocks {

    private ExternalServiceMocks() {}

    /* ---------- /auth ---------- */

    public static void authSuccess() {
        stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse().withStatus(200)));
    }

    public static void authFailure(int status) {
        stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse().withStatus(status)));
    }

    /* ---------- /doAction ---------- */

    public static void doActionSuccess() {
        stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(200)));
    }

    public static void doActionFailure(int status) {
        stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(status)));
    }
}