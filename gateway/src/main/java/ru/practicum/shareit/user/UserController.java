package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.dto.user.UserDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * RestController для работы приложения по пути /users
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользователи")
public class UserController {
    private final UserClient client;

    @Operation(summary = "Создать пользователя",
            description = """
                    Создание нового пользователя по заданным email и имени.
                    Email уникальный, в формате user@email.com
                    """)
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@RequestBody @Valid final UserDto userDto) {
        log.info("Post user");
        return client.createUser(userDto);
    }

    @Operation(summary = "Обновить пользователя",
            description = """
                    Обновление данных пользователя.
                    null значения игнорируются.
                    Можно обновить email, но на такой же уникальный
                    Email в формате user@email.com
                    """)
    @PatchMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@Parameter(description = "id пользователя")
                                             @PathVariable final Long userId,
                                             @RequestBody @Valid final UserDto userDto) {

        log.info("PATCH update user; userId={}", userId);
        return client.updateUser(userId, userDto);
    }

    @Operation(summary = "Вернуть пользователя по id",
            description = """
                    Получение данных о пользователе по id.
                    Возвращаются id, имя и email
                    """)
    @GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(@Parameter(description = "id пользователя")
                                          @PathVariable final Long userId) {

        log.info("GET user; userId={}", userId);
        return client.getUser(userId);
    }

    @Operation(summary = "Удалить пользователя",
            description = """
                    Удаление пользователя по id.
                    Удаляются все данные о пользователе из БД
                    """)
    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> delUser(@Parameter(description = "id пользователя")
                                          @PathVariable final Long userId) {

        log.info("DELETE user; userId={}", userId);
        return client.delUser(userId);
    }

    @Operation(summary = "Вернуть список всех пользователей",
            description = """
                    Возвращается страница № from размера size из БД
                    """)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllUsers(@Parameter(description = "№ страницы")
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                              @Parameter(description = "Размер страницы")
                                              @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        log.info("GET all users; from={}, size={}", from, size);
        return client.getAllUsers(from, size);
    }
}