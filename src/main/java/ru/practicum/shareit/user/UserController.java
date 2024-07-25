package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectEmailException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid final UserDto userDto) {

        log.info("UserController: createUser");
        try {
            return userService.createUser(userDto);
        } catch (FailEmailException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка редактирования пользователя! " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable final long userId,
                              @RequestBody @Valid final UserDto userDto) {

        log.info("UserController: updateUser");
        try {
            return userService.updateUser(userId, userDto);

        } catch (IncorrectUserIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ошибка редактирования пользователя! " + e.getMessage());

        } catch (IncorrectEmailException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ошибка создания пользователя! " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable final long userId) {

        log.info("UserController: getUser");
        try {
            return userService.getUser(userId);
        } catch (IncorrectUserIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ошибка поиска пользователя! " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public UserDto delUser(@PathVariable final long userId) {

        log.info("UserController: delUser");
        return userService.delUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        log.info("UserController: getAllUsers");
        return userService.getAllUsers();
    }
}
