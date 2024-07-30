package ru.practicum.shareit.user.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.user.UserRepositoryOld;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Интерфейс репозитория для хранения информации об User классах
 * в оперативной памяти
 * Устаревший, не используется
 */
@Component
@Slf4j
public class UserRepositoryOldImpl implements UserRepositoryOld {
    private final Map<Long, User> repos;
    private long idCount;
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryOldImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.repos = new HashMap<>();
        idCount = 0;
    }

    @Override
    public UserDto createUser(final UserDto userDto) throws FailEmailException {
        checkingUsersEmail(userDto, "createUser");

        User user = userMapper.toUser(userDto);
        user.setId(++idCount);
        repos.put(idCount, user);
        log.info("UserRepositoryImpl: createUser, idUser: " + idCount);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(final UserDto userDto) throws FailEmailException {
        checkingUsersEmail(userDto, "updateUser");

        User user = repos.get(userDto.getId());
        User updUser = userMapper.toUser(userDto);

        if (updUser.getName() != null) {
            user.setName(updUser.getName());
        }
        if (updUser.getEmail() != null) {
            user.setEmail(updUser.getEmail());
        }
        log.info("UserRepositoryImpl: updateUser, idUser: " + idCount);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(final long userId) {

        log.info("UserRepositoryImpl: getUser, idUser: " + userId);
        return userMapper.toUserDto(repos.get(userId));
    }

    @Override
    public UserDto delUser(final long userId) {

        log.info("UserRepositoryImpl: delUser, idUser: " + userId);
        return userMapper.toUserDto(repos.remove(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {

        log.info("UserRepositoryImpl: getAllUsers");
        return repos.values().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkingUsersEmail(final UserDto userDto, final String method)
            throws FailEmailException {

        for (User user : repos.values()) {
            if (user.getEmail().equals(userDto.getEmail()) && !user.getId().equals(userDto.getId())) {
                log.warn("UserRepositoryImpl: " + method + " FALSE, Incorrect user email");
                throw new FailEmailException("Пользователь с email " + userDto.getEmail() + " уже существует.");
            }
        }
    }
}
