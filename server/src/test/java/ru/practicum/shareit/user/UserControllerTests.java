package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.DataException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.ResponseToUserDeletion;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    void createUserTest() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(new UserDto(1L, "user", "user@email.com"));

        UserDto userRequest = new UserDto(null, "user", "user@email.com");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.email").value("user@email.com"));
    }

    @Test
    void createUserWithSameEmailTest() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new DataException("Пользователь с email user@email.com уже существует"));

        UserDto userRequest = new UserDto(null, "user", "user@email.com");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Пользователь с email user@email.com уже существует"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void getUserTest() throws Exception {
        when(userService.getUser(anyLong()))
                .thenReturn(new UserDto(1L, "user", "user@email.com"));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.email").value("user@email.com"));
    }

    @Test
    void getUserWithIncorrectUserIdTest() throws Exception {
        when(userService.getUser(anyLong()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/users"));

    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(new UserDto(1L, "updUser", "updUser@email.com"));

        UserDto userRequest = new UserDto(null, "updUser", "updUser@email.com");
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updUser"))
                .andExpect(jsonPath("$.email").value("updUser@email.com"));
    }

    @Test
    void updateUserWithSameEmailTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new DataException("Пользователь с email user@email.com уже существует"));

        UserDto userRequest = new UserDto(null, "updUser", "updUser@email.com");
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Пользователь с email user@email.com уже существует"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void updateWithIncorrectUserIdTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        UserDto userRequest = new UserDto(null, "updUser", "updUser@email.com");
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void delUserTest() throws Exception {
        when(userService.delUser(anyLong()))
                .thenReturn(new ResponseToUserDeletion(200, "Пользователь успешно удален", "/users"));

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Пользователь успешно удален"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void getAllUsersTest() throws Exception {
        UserDto userDto1 = new UserDto(1L, "name1", "user1@email.com");
        UserDto userDto2 = new UserDto(2L, "name2", "user2@email.com");
        UserDto userDto3 = new UserDto(3L, "name3", "user3@email.com");
        UserDto userDto4 = new UserDto(4L, "name4", "user4@email.com");
        UserDto userDto5 = new UserDto(5L, "name5", "user5@email.com");
        List<UserDto> users = List.of(userDto1, userDto2, userDto3, userDto4, userDto5);

        when(userService.getAllUsers(anyInt(), anyInt()))
                .thenReturn(users);

        mvc.perform(get("/users?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value("1"))
                .andExpect(jsonPath("[0].name").value("name1"))
                .andExpect(jsonPath("[0].email").value("user1@email.com"))

                .andExpect(jsonPath("[1].id").value("2"))
                .andExpect(jsonPath("[1].name").value("name2"))
                .andExpect(jsonPath("[1].email").value("user2@email.com"))

                .andExpect(jsonPath("[2].id").value("3"))
                .andExpect(jsonPath("[2].name").value("name3"))
                .andExpect(jsonPath("[2].email").value("user3@email.com"))

                .andExpect(jsonPath("[3].id").value("4"))
                .andExpect(jsonPath("[3].name").value("name4"))
                .andExpect(jsonPath("[3].email").value("user4@email.com"))

                .andExpect(jsonPath("[4].id").value("5"))
                .andExpect(jsonPath("[4].name").value("name5"))
                .andExpect(jsonPath("[4].email").value("user5@email.com"));
    }
}
