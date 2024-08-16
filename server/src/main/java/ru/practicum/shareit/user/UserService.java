package ru.practicum.shareit.user;

import ru.practicum.shareit.model.dto.user.UserDto;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;

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
    UserDto updateUser(Long userId, UserDto userDto);

    /**
     * Вернуть пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return искомый пользователь
     */
    UserDto getUser(Long userId);

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя
     */
    ResponseToUserDeletion delUser(Long userId);

    /**
     * Вернут список всех пользователей.
     *
     * @return список всех пользователей
     */
    List<UserDto> getAllUsers(Integer from, Integer size);
}
