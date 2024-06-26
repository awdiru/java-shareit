package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectEmailException;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto UserDto) {

        log.info("UserController: createUser");
        try {
            return userService.createUser(UserDto);

        } catch (IncorrectEmailException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ошибка создания пользователя! " + e.getMessage());

        } catch (FailEmailException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка редактирования пользователя! " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId,
                              @RequestBody @Valid UserDto UserDto) {

        log.info("UserController: updateUser");
        try {
            return userService.updateUser(userId, UserDto);

        } catch (IncorrectUserIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ошибка редактирования пользователя! " + e.getMessage());

        } catch (IncorrectEmailException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ошибка создания пользователя! " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {

        log.info("UserController: getUser");
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto delUser(@PathVariable long userId) {

        log.info("UserController: delUser");
        return userService.delUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        log.info("UserController: getAllUsers");
        return userService.getAllUsers();
    }
}
