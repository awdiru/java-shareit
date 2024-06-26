package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.IncorrectEmailException;

import java.util.List;

public interface UserRepository {
    /**
     * Создать нового пользователя.
     *
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto createUser(UserDto userDto) throws IncorrectEmailException;

    /**
     * Обновить пользователя.
     *
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto updateUser(UserDto userDto) throws IncorrectEmailException;

    /**
     * Вернуть пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return искомый пользователь
     */
    UserDto getUser(long userId);

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
