package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

/**
 * Конвертер User классов
 */
@Component
public class UserMapper {
    public UserDto toUserDto(final User user) {

        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User toUser(final UserDto userDto) {

        if (userDto == null) {
            return null;
        }
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
