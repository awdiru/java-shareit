package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс сервиса для UserController
 */
public interface UserService {
    /**
     * Создать нового пользователя.
     *
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto createUser(UserDto userDto) throws FailEmailException;

    /**
     * Обновить пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto updateUser(long userId, UserDto userDto) throws IncorrectUserIdException;

    /**
     * Вернуть пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return искомый пользователь
     */
    UserDto getUser(long userId) throws IncorrectUserIdException;

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя
     * @return удаленный пользователь
     */
    UserDto delUser(long userId);

    /**
     * Вернут список всех пользователей.
     *
     * @return список всех пользователей
     */
    List<UserDto> getAllUsers();
}
