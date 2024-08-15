package ru.practicum.shareit.user.impliments;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Реализация сервиса для ItemController
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(final UserDto userDto) {
        try {
            User user = userRepository.save(userMapper.toUser(userDto));
            return userMapper.toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto updateUser(final Long userId,
                              final UserDto userDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException(
                        "Пользователь с id " + userId + " не найден."));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        try {
            return userMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DataException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto getUser(final Long userId) {
        return userMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException(
                        "Пользователь с id " + userId + " не найден.")));
    }

    @Override
    public ResponseToUserDeletion delUser(final Long userId) {
        userRepository.deleteById(userId);
        return new ResponseToUserDeletion(200, "Пользователь успешно удален", "/users");
    }

    @Override
    public List<UserDto> getAllUsers(final Integer from,
                                     final Integer size) {

        return userRepository.findAll(PageRequest.of(from, size))
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }
}