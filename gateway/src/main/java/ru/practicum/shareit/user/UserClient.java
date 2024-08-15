package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.IncorrectEmailException;

import java.util.Map;

/**
 * Клиент приложения по пути /users
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Запрос на создание пользователя
     *
     * @param userDto информация о новом пользователе
     * @return ответ сервера
     */
    public ResponseEntity<Object> createUser(final UserDto userDto) {

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty())
            throw new IncorrectEmailException("Email не может быть пустым.");

        return post("", userDto);
    }

    /**
     * Обновление данных о пользователе
     *
     * @param userId  id пользователя
     * @param userDto обновленный данные о пользователе
     * @return ответ сервера
     */
    public ResponseEntity<Object> updateUser(final Long userId,
                                             final UserDto userDto) {

        return patch("/" + userId, userDto);
    }

    /**
     * Вернуть данные о пользователе
     *
     * @param userId id пользователя
     * @return ответ сервера
     */
    public ResponseEntity<Object> getUser(final Long userId) {
        return get("/" + userId);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     * @return ответ сервера
     */
    public ResponseEntity<Object> delUser(final Long userId) {
        return delete("/" + userId);
    }

    /**
     * Вернуть список всех пользователей
     *
     * @param from индекс страницы (0 по умолчанию)
     * @param size размер страницы (10 по умолчанию)
     * @return ответ сервера
     */
    public ResponseEntity<Object> getAllUsers(final Integer from,
                                              final Integer size) {

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", null, parameters);
    }
}