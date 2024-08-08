package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Базовый класс клиента
 * Реализует все базовые CRUD операции
 */
public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Запрос с методом GET
     *
     * @param path       путь запроса
     * @param userId     id пользователя
     * @param parameters список параметров
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    /**
     * Запрос с методом GET
     *
     * @param path путь запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    /**
     * Запрос с методом GET
     *
     * @param path   путь запроса
     * @param userId id пользователя
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    /**
     * Запрос с методом POST
     *
     * @param path       путь запроса
     * @param userId     id пользователя
     * @param parameters список параметров
     * @param body       тело запроса
     * @param <T>        сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    /**
     * Запрос с методом POST
     *
     * @param path путь запроса
     * @param body тело запроса
     * @param <T>  сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    /**
     * Запрос с методом POST
     *
     * @param path   путь запроса
     * @param userId id пользователя
     * @param body   тело запроса
     * @param <T>    сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    /**
     * Запрос с методом PATCH
     *
     * @param path       путь запроса
     * @param userId     id пользователя
     * @param parameters список параметров
     * @param body       тело запроса
     * @param <T>        сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    /**
     * Запрос с методом PATCH
     *
     * @param path путь запроса
     * @param body тело запроса
     * @param <T>  сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    /**
     * Запрос с методом PATCH
     *
     * @param path   путь запроса
     * @param userId id пользователя
     * @param body   тело запроса
     * @param <T>    сущность тела запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    /**
     * Запрос с методом DELETE
     *
     * @param path       путь запроса
     * @param userId     Id пользователя
     * @param parameters список параметров
     * @return ответ сервера
     */
    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    /**
     * Запрос с методом DELETE
     *
     * @param path путь запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    /**
     * Создает и отправляет запрос на сервер
     *
     * @param method     метод запроса
     * @param path       путь запроса
     * @param userId     id пользователя
     * @param parameters список параметров
     * @param body       тело запроса
     * @param <T>        сущность тела запроса
     * @return ответ сервера
     */
    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    /**
     * Возвращает сет стандартных заголовков
     *
     * @param userId id пользователя
     * @return сет стандартных заголовков
     */
    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    /**
     * Обрабатывает ответ сервера
     *
     * @param response необработанный ответ
     * @return обработанный ответ
     */
    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}