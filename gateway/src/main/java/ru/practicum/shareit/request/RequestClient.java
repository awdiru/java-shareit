package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.model.dto.request.RequestIncDto;

import java.util.Map;

/**
 * Клиент приложения по пути /requests
 */
@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создать запрос на вещь
     *
     * @param userId     id пользователя
     * @param requestDto запрос на вещь
     * @return ответ сервера
     */
    public ResponseEntity<Object> createRequest(final Long userId,
                                                final RequestIncDto requestDto) {

        return post("", userId, requestDto);
    }

    /**
     * Вернуть все запросы пользователя с ответами на них
     *
     * @param userId id пользователя
     * @return ответ сервера
     */
    public ResponseEntity<Object> getRequestsUser(final Long userId) {
        return get("", userId);
    }

    /**
     * Вернуть все запросы других пользователей
     *
     * @param userId id пользователя
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return ответ сервера
     */
    public ResponseEntity<Object> getAllRequests(final Long userId,
                                                 final Integer from,
                                                 final Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    /**
     * Вернуть конкретный запрос
     *
     * @param requestId id запроса
     * @return ответ сервера
     */
    public ResponseEntity<Object> getRequest(final Long requestId) {
        return get("/" + requestId);
    }
}
