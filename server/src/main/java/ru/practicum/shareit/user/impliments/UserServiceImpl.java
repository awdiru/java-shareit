package ru.practicum.shareit.user.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;
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
    public UserDto createUser(final UserDto userDto) {
        try {
            User user = repos.save(userMapper.toUser(userDto));
            return userMapper.toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto updateUser(final Long userId, final UserDto userDto) {

        User user = repos.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException(
                        "Пользователь с id " + userId + " не найден."));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        try {
            return userMapper.toUserDto(repos.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto getUser(final Long userId) {
        return userMapper.toUserDto(repos.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException(
                        "Пользователь с id " + userId + " не найден.")));
    }

    @Override
    public ResponseToUserDeletion delUser(final Long userId) {
        repos.deleteById(userId);
        return new ResponseToUserDeletion(200, "Пользователь успешно удален", "/users");
    }

    @Override
    public List<UserDto> getAllUsers(Integer from, Integer size) {
        Pageable paging = PageRequest.of(from, size);
        return repos.findAll(paging)
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
