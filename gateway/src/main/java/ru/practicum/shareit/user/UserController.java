package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * RestController для работы приложения по пути /users
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient client;

    /**
     * Запрос на создание пользователя
     *
     * @param userDto информация о новом пользователе
     * @return ответ сервера
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid final UserDto userDto) {
        log.info("Post user");
        return client.createUser(userDto);
    }

    /**
     * Обновление данных о пользователе
     *
     * @param userId  id пользователя
     * @param userDto обновленный данные о пользователе
     * @return ответ сервера
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable final Long userId,
                                             @RequestBody @Valid final UserDto userDto) {

        log.info("PATCH update user; userId={}", userId);
        return client.updateUser(userId, userDto);
    }

    /**
     * Вернуть данные о пользователе
     *
     * @param userId id пользователя
     * @return ответ сервера
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable final Long userId) {

        log.info("GET user; userId={}", userId);
        return client.getUser(userId);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     * @return ответ сервера
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delUser(@PathVariable final Long userId) {

        log.info("DELETE user; userId={}", userId);
        return client.delUser(userId);
    }

    /**
     * Вернуть список всех пользователей
     *
     * @param from индекс страницы (0 по умолчанию)
     * @param size размер страницы (10 по умолчанию)
     * @return ответ сервера
     */
    @GetMapping
    public ResponseEntity<Object> getAllUsers(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("GET all users; from={}, size={}", from, size);
        return client.getAllUsers(from, size);
    }
}