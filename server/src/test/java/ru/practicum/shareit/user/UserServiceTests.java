package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;

import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {
    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDtoMust = new UserDto(null, "user", "user@email.com");
        UserDto userDtoIs = userService.createUser(userDtoMust);

        Assertions.assertEquals(userDtoIs.getName(), userDtoMust.getName());
        Assertions.assertEquals(userDtoIs.getEmail(), userDtoMust.getEmail());
    }

    @Test
    void createUserWithSameEmail() {
        userService.createUser(new UserDto(null, "user1", "sameUser@email.com"));

        DataException e = Assertions.assertThrows(
                DataException.class,
                () -> userService.createUser(new UserDto(null, "user2", "sameUser@email.com"))
        );
        Assertions.assertEquals("Пользователь с email sameUser@email.com уже существует", e.getMessage());
    }

    @Test
    void getUserTest() {
        UserDto userDtoMust = new UserDto(null, "user", "user@email.com");
        Long id = userService.createUser(userDtoMust).getId();
        UserDto userDtoIs = userService.getUser(id);

        Assertions.assertEquals(userDtoIs.getName(), userDtoMust.getName());
        Assertions.assertEquals(userDtoIs.getEmail(), userDtoMust.getEmail());
    }

    @Test
    void getUserWithIncorrectUserId() {
        UserDto userDtoMust = new UserDto(null, "user", "user@email.com");
        Long id = userService.createUser(userDtoMust).getId() + 100;

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> userService.getUser(id)
        );
        Assertions.assertEquals("Пользователь с id " + id + " не найден.", e.getMessage());

    }

    @Test
    void updateUserTest() {
        Long id = userService.createUser(new UserDto(null, "user", "user@email.com")).getId();
        UserDto userDtoMust = new UserDto(null, "updName", "updUser@email.com");
        userService.updateUser(id, userDtoMust);
        UserDto userDtoIs = userService.getUser(id);

        Assertions.assertEquals(userDtoIs.getName(), userDtoMust.getName());
        Assertions.assertEquals(userDtoIs.getEmail(), userDtoMust.getEmail());
    }

    @Test
    void updateUserWithIncorrectUserIdTest() {
        Long id = userService.createUser(new UserDto(null, "user", "user@email.com")).getId() + 100;
        UserDto userDtoMust = new UserDto(null, "updName", "updUser@email.com");

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> userService.updateUser(id, userDtoMust)
        );
        Assertions.assertEquals("Пользователь с id " + id + " не найден.", e.getMessage());

    }

    @Test
    void delUserTest() {
        Long id = userService.createUser(new UserDto(null, "user", "user@email.com")).getId() + 100;
        ResponseToUserDeletion response = userService.delUser(id);

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> userService.getUser(id)
        );
        Assertions.assertEquals("Пользователь с id " + id + " не найден.", e.getMessage());
        Assertions.assertEquals(response.getMessage(), "Пользователь успешно удален");
        Assertions.assertEquals(response.getStatus(), 200);
        Assertions.assertEquals(response.getPath(), "/users");
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto1 = new UserDto(null, "name1", "user1@email.com");
        UserDto userDto2 = new UserDto(null, "name2", "user2@email.com");
        UserDto userDto3 = new UserDto(null, "name3", "user3@email.com");
        UserDto userDto4 = new UserDto(null, "name4", "user4@email.com");
        UserDto userDto5 = new UserDto(null, "name5", "user5@email.com");

        userDto1.setId(userService.createUser(userDto1).getId());
        userDto2.setId(userService.createUser(userDto2).getId());
        userDto3.setId(userService.createUser(userDto3).getId());
        userDto4.setId(userService.createUser(userDto4).getId());
        userDto5.setId(userService.createUser(userDto5).getId());


        List<UserDto> allUsersMust = List.of(userDto1, userDto2, userDto3, userDto4, userDto5);
        List<UserDto> allUsersIs = userService.getAllUsers(0, 1000);

        Assertions.assertEquals(allUsersMust, allUsersIs);

        allUsersIs = userService.getAllUsers(0, 2);
        allUsersMust = List.of(userDto1, userDto2);

        Assertions.assertEquals(allUsersMust, allUsersIs);

        allUsersIs = userService.getAllUsers(1, 2);
        allUsersMust = List.of(userDto3, userDto4);

        Assertions.assertEquals(allUsersMust, allUsersIs);

        allUsersIs = userService.getAllUsers(3, 2);
        allUsersMust = List.of();

        Assertions.assertEquals(allUsersMust, allUsersIs);
    }
}
