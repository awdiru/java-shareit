package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.DataException;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.annotations.UserControllerExceptionHandler;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * RestController для работы приложения по пути /users
 */
@RestController
@Slf4j
@RequestMapping("/users")
@UserControllerExceptionHandler
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Запрос на создание пользователя
     *
     * @param userDto информация о новом пользователе
     * @return созданный пользователь
     * @throws FailEmailException исключение при попытке
     *                            создать пользователя пустым email
     * @throws DataException      Ошибка базы данных, при попытке создать пользователя с существующим email
     */
    @PostMapping
    public UserDto createUser(@RequestBody @Valid final UserDto userDto) throws FailEmailException, DataException {
        log.info("UserController: createUser");
        return userService.createUser(userDto);
    }

    /**
     * Обновление данных о пользователе
     *
     * @param userId  id пользователя
     * @param userDto обновленный данные о пользователе
     * @return обновленный пользователь
     * @throws IncorrectUserIdException некорректный id пользователя
     * @throws DataException            Ошибка базы данных, при попытке обновить email пользователя на уже существующий
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable final long userId,
                              @RequestBody @Valid final UserDto userDto)
            throws IncorrectUserIdException, DataException {

        log.info("UserController: updateUser");
        return userService.updateUser(userId, userDto);

    }

    /**
     * Вернуть данные о пользователе
     *
     * @param userId id пользователя
     * @return запрашиваемые данные
     * @throws IncorrectUserIdException некорректный id пользователя
     */
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable final long userId) throws IncorrectUserIdException {

        log.info("UserController: getUser");
        return userService.getUser(userId);

    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     * @return удаленный пользователь
     */
    @DeleteMapping("/{userId}")
    public UserDto delUser(@PathVariable final long userId) {

        log.info("UserController: delUser");
        return userService.delUser(userId);
    }

    /**
     * Вернуть список всех пользователей
     *
     * @return список всех пользователей
     */
    @GetMapping
    public List<UserDto> getAllUsers() {

        log.info("UserController: getAllUsers");
        return userService.getAllUsers();
    }

}
