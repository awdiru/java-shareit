package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.DataException;
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
     * @throws DataException      ошибка базы данных, при попытке создать пользователя с существующим email
     * @throws FailEmailException при попытке создать пользователя с пустым email
     */
    UserDto createUser(UserDto userDto) throws FailEmailException, DataException;

    /**
     * Обновить пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param userDto новый пользователь
     * @return новый пользователь
     * @throws IncorrectUserIdException при попытке обновить пользователя с несуществующим Id
     * @throws DataException            ошибка базы данных, при попытке обновить email на существующий
     */
    UserDto updateUser(long userId, UserDto userDto) throws IncorrectUserIdException, DataException;

    /**
     * Вернуть пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return искомый пользователь
     * @throws IncorrectUserIdException при попытке запросить пользователя с несуществующим Id
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
