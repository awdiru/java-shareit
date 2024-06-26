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
    public UserServiceImpl(UserRepository repos) {
        this.repos = repos;
    }

    @Override
    public UserDto createUser(UserDto UserDto) throws IncorrectEmailException, FailEmailException {

        if (UserDto.getEmail() == null || UserDto.getEmail().isEmpty()) {
            log.warn("UserServiceImpl: createUser FALSE, fail email");
            throw new FailEmailException("Email не может быть пустым.");
        }
        log.info("UserServiceImpl: createUser");
        return repos.createUser(UserDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto UserDto)
            throws IncorrectUserIdException, IncorrectEmailException {

        if (getUser(userId) == null) {
            log.warn("UserServiceImpl: updateUser FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с идентификатором " + UserDto.getId() + " не найден.");
        }
        UserDto.setId(userId);
        log.info("UserServiceImpl: updateUser");
        return repos.updateUser(UserDto);
    }

    @Override
    public UserDto getUser(long userId) {

        log.info("UserServiceImpl: getUser");
        return repos.getUser(userId);
    }

    @Override
    public UserDto delUser(long userId) {

        log.info("UserServiceImpl: delUser");
        return repos.delUser(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {

        log.info("UserServiceImpl: getAllUsers");
        return repos.getAllUsers();
    }
}
