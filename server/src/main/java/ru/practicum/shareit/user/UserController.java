package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.dto.user.UserDto;
import ru.practicum.shareit.user.annotations.UserControllerExceptionHandler;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;

import java.util.List;

/**
 * RestController для работы приложения по пути /users
 */
@RestController
@Slf4j
@RequestMapping("/users")
@UserControllerExceptionHandler
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Запрос на создание пользователя
     *
     * @param userDto информация о новом пользователе
     * @return созданный пользователь
     */
    @PostMapping
    public UserDto createUser(@RequestBody final UserDto userDto) {
        log.info("Create user");
        return userService.createUser(userDto);
    }

    /**
     * Обновление данных о пользователе
     *
     * @param userId  id пользователя
     * @param userDto обновленный данные о пользователе
     * @return обновленный пользователь
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable final Long userId,
                              @RequestBody final UserDto userDto) {

        log.info("PATCH update user; userId={}", userId);
        return userService.updateUser(userId, userDto);
    }

    /**
     * Вернуть данные о пользователе
     *
     * @param userId id пользователя
     * @return запрашиваемые данные
     */
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable final Long userId) {

        log.info("Get user; userId={}", userId);
        return userService.getUser(userId);

    }

    /**
     * Удалить пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseToUserDeletion delUser(@PathVariable final Long userId) {

        log.info("Delete user; userId={}", userId);
        return userService.delUser(userId);
    }

    /**
     * Вернуть список всех пользователей
     *
     * @param from индекс страницы (0 по умолчанию)
     * @param size размер страницы (10 по умолчанию)
     * @return список @size пользователей, страница @from
     */
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam final Integer from,
                                     @RequestParam final Integer size) {

        log.info("Get all users; from={}, size={}", from, size);
        return userService.getAllUsers(from, size);
    }
}
