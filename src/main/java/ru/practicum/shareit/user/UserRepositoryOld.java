package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс репозитория для хранения информации об User классах
 * в оперативной памяти
 * Устаревший, не используется
 */
public interface UserRepositoryOld {
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
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto updateUser(UserDto userDto) throws FailEmailException;

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
