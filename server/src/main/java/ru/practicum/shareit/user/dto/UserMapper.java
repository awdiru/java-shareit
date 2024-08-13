package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

/**
 * Конвертер User классов
 */
@Component
public class UserMapper {
    public UserDto toUserDto(final User user) {
        return user == null ? null :
                new UserDto(user.getId(),
                        user.getName(),
                        user.getEmail());
    }

    public User toUser(final UserDto userDto) {
        return userDto == null ? null :
                new User(userDto.getId(),
                        userDto.getName(),
                        userDto.getEmail());
    }
}