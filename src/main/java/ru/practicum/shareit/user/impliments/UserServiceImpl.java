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
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repos;

    @Autowired
    public UserServiceImpl(final UserRepository repos) {
        this.repos = repos;
    }

    @Override
    public UserDto createUser(final UserDto userDto) throws FailEmailException {

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            log.warn("UserServiceImpl: createUser FALSE, fail email");
            throw new FailEmailException("Email не может быть пустым.");
        }
        log.info("UserServiceImpl: createUser");
        User user = repos.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(final long userId, final UserDto userDto)
            throws IncorrectUserIdException {

        if (getUser(userId) == null) {
            log.warn("UserServiceImpl: updateUser FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с идентификатором " + userDto.getId() + " не найден.");
        }
        log.info("UserServiceImpl: updateUser");
        userDto.setId(userId);
        User user = repos.findById(userId);
        if (userDto.getName() != null)
            user.setName(userDto.getName());
        if (userDto.getEmail() != null)
            user.setEmail(userDto.getEmail());
        return UserMapper.toUserDto(repos.save(user));
    }

    @Override
    public UserDto getUser(final long userId) throws IncorrectUserIdException {
        log.info("UserServiceImpl: getUser");
        UserDto userDto = UserMapper.toUserDto(repos.findById(userId));
        if (userDto != null)
            return userDto;
        else throw new IncorrectUserIdException("Пользователь с идентификатором " + userId + " не найден.");
    }

    @Override
    public UserDto delUser(final long userId) {
        log.info("UserServiceImpl: delUser");
        return UserMapper.toUserDto(repos.deleteById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("UserServiceImpl: getAllUsers");
        return repos.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
