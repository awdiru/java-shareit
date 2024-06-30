package ru.practicum.shareit.user.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectEmailException;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repos;

    @Autowired
    public UserServiceImpl(final UserRepository repos) {
        this.repos = repos;
    }

    @Override
    public UserDto createUser(final UserDto userDto) throws IncorrectEmailException, FailEmailException {

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            log.warn("UserServiceImpl: createUser FALSE, fail email");
            throw new FailEmailException("Email не может быть пустым.");
        }
        log.info("UserServiceImpl: createUser");
        return repos.createUser(userDto);
    }

    @Override
    public UserDto updateUser(final long userId, final UserDto userDto)
            throws IncorrectUserIdException, IncorrectEmailException {

        if (getUser(userId) == null) {
            log.warn("UserServiceImpl: updateUser FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с идентификатором " + userDto.getId() + " не найден.");
        }
        userDto.setId(userId);
        log.info("UserServiceImpl: updateUser");
        return repos.updateUser(userDto);
    }

    @Override
    public UserDto getUser(final long userId) {

        log.info("UserServiceImpl: getUser");
        return repos.getUser(userId);
    }

    @Override
    public UserDto delUser(final long userId) {

        log.info("UserServiceImpl: delUser");
        return repos.delUser(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {

        log.info("UserServiceImpl: getAllUsers");
        return repos.getAllUsers();
    }
}
