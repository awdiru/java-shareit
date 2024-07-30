package ru.practicum.shareit.user.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DataException;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для ItemController
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repos;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(final UserRepository repos,
                           final UserMapper userMapper) {
        this.repos = repos;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(final UserDto userDto)
            throws FailEmailException, DataException {

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            log.warn("UserServiceImpl: createUser FALSE, FailEmailException");
            throw new FailEmailException("Email не может быть пустым.");
        }
        log.info("UserServiceImpl: createUser");
        try {
            User user = repos.save(userMapper.toUser(userDto));
            return userMapper.toUserDto(user);
        } catch (Exception e) {
            log.warn("UserServiceImpl: createUser FALSE, DataException");
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto updateUser(final long userId, final UserDto userDto)
            throws IncorrectUserIdException, DataException {

        if (getUser(userId) == null) {
            log.warn("UserServiceImpl: updateUser FALSE, IncorrectUserIdException");
            throw new IncorrectUserIdException("Ошибка редактирования пользователя! "
                    + "Пользователь с идентификатором " + userDto.getId() + " не найден.");
        }
        log.info("UserServiceImpl: updateUser");
        userDto.setId(userId);
        User user = repos.findById(userId);
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        try {
            return userMapper.toUserDto(repos.save(user));
        } catch (Exception e) {
            log.warn("UserServiceImpl: updateUser FALSE, DataException");
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto getUser(final long userId)
            throws IncorrectUserIdException {

        UserDto userDto = userMapper.toUserDto(repos.findById(userId));
        if (userDto != null) {
            log.info("UserServiceImpl: getUser");
            return userDto;
        } else {
            log.warn("UserServiceImpl: getUser FALSE, IncorrectUserIdException");
            throw new IncorrectUserIdException("Пользователь с идентификатором " + userId + " не найден.");
        }
    }

    @Override
    public UserDto delUser(final long userId) {
        log.info("UserServiceImpl: delUser");
        return userMapper.toUserDto(repos.deleteById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("UserServiceImpl: getAllUsers");
        return repos.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
