package ru.practicum.shareit.user;

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
    UserDto createUser(UserDto userDto);

    /**
     * Обновить пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param userDto новый пользователь
     * @return новый пользователь
     */
    UserDto updateUser(long userId, UserDto userDto);

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
