package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.item.dto.model.comment.CommentIncDto;
import ru.practicum.shareit.item.dto.model.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.model.item.ItemIncDto;
import ru.practicum.shareit.item.dto.model.item.ItemOutDto;
import ru.practicum.shareit.item.dto.model.item.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService itemService;
    private final String userIdHead = "X-Sharer-User-Id";

    @Test
    void createItemTest() throws Exception {
        UserDto owner = getOwner();
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(new ItemOutDto(1L, "item", "description",
                        owner, null, true, null, List.of()));

        ItemIncDto itemIncDto = new ItemIncDto("item", "description", true, null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.id").value(1L))
                .andExpect(jsonPath("$.owner.name").value("user"))
                .andExpect(jsonPath("$.owner.email").value("user@email.com"));
    }

    @Test
    void createItemWithIncorrectUserIdTest() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        ItemIncDto itemIncDto = new ItemIncDto("item", "description", true, null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/items"));
    }

    @Test
    void createItemWithIncorrectRequestIdTest() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenThrow(new IncorrectRequestIdException("Запрос с id 1 не найден"));

        ItemIncDto itemIncDto = new ItemIncDto("item", "description", true, null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Запрос с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/items"));
    }

    @Test
    void updateUser() throws Exception {
        UserDto owner = getOwner();
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(new ItemOutDto(1L, "updItem", "updDescription",
                        owner, null, true, null, List.of()));

        ItemIncDto itemIncDto = new ItemIncDto("updItem", "updDescription", true, null);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updItem"))
                .andExpect(jsonPath("$.description").value("updDescription"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.id").value(1L))
                .andExpect(jsonPath("$.owner.name").value("user"))
                .andExpect(jsonPath("$.owner.email").value("user@email.com"));
    }

    @Test
    void updateItemWithIncorrectUserIdTest() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenThrow(new IncorrectUserIdException("Редактировать вещь может только ее владелец."));

        ItemIncDto itemIncDto = new ItemIncDto("updItem", "updDescription", true, null);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Редактировать вещь может только ее владелец."))
                .andExpect(jsonPath("$.path").value("/items"));
    }

    @Test
    void updateItemWithIncorrectItemIdTest() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenThrow(new IncorrectItemIdException("Вещь с id 1 не найдена."));

        ItemIncDto itemIncDto = new ItemIncDto("updItem", "updDescription", true, null);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Вещь с id 1 не найдена."))
                .andExpect(jsonPath("$.path").value("/items"));
    }

    @Test
    void getItemTest() throws Exception {
        UserDto owner = getOwner();
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(new ItemWidthBookingsTimeDto(1L, "item", "description",
                        owner, null, true, null, null, null, List.of()));

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner.id").value(1L))
                .andExpect(jsonPath("$.owner.name").value("user"))
                .andExpect(jsonPath("$.owner.email").value("user@email.com"));
    }

    @Test
    void getItemWithIncorrectItemId() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenThrow(new IncorrectItemIdException("Вещь с id 1 не найдена."));

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Вещь с id 1 не найдена."))
                .andExpect(jsonPath("$.path").value("/items"));
    }

    @Test
    void getItemsUserTest() throws Exception {
        List<ItemWidthBookingsTimeDto> items = getItemsWidthBookingsTimeDto();
        when(itemService.getItemsUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(items);

        mvc.perform(get("/items?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1L))
                .andExpect(jsonPath("[1].id").value(2L))
                .andExpect(jsonPath("[2].id").value(3L))
                .andExpect(jsonPath("[3].id").value(4L))
                .andExpect(jsonPath("[4].id").value(5L))

                .andExpect(jsonPath("[0].name").value("item1"))
                .andExpect(jsonPath("[1].name").value("item2"))
                .andExpect(jsonPath("[2].name").value("item3"))
                .andExpect(jsonPath("[3].name").value("item4"))
                .andExpect(jsonPath("[4].name").value("item5"))

                .andExpect(jsonPath("[0].description").value("description1"))
                .andExpect(jsonPath("[1].description").value("description2"))
                .andExpect(jsonPath("[2].description").value("description3"))
                .andExpect(jsonPath("[3].description").value("description4"))
                .andExpect(jsonPath("[4].description").value("description5"))

                .andExpect(jsonPath("[0].owner.id").value(1L))
                .andExpect(jsonPath("[0].owner.name").value("user"))
                .andExpect(jsonPath("[0].owner.email").value("user@email.com"))

                .andExpect(jsonPath("[0].available").value(true))
                .andExpect(jsonPath("[1].available").value(true))
                .andExpect(jsonPath("[2].available").value(true))
                .andExpect(jsonPath("[3].available").value(true))
                .andExpect(jsonPath("[4].available").value(true))

                .andExpect(jsonPath("[0].comments").isEmpty())
                .andExpect(jsonPath("[1].comments").isEmpty())
                .andExpect(jsonPath("[2].comments").isEmpty())
                .andExpect(jsonPath("[3].comments").isEmpty())
                .andExpect(jsonPath("[4].comments").isEmpty());
    }

    @Test
    void searchItemsTest() throws Exception {
        List<ItemOutDto> items = getItemsOutDto();
        when(itemService.searchItems(any()))
                .thenReturn(items);

        mvc.perform(get("/items/search?text=item")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1L))
                .andExpect(jsonPath("[1].id").value(2L))
                .andExpect(jsonPath("[2].id").value(3L))
                .andExpect(jsonPath("[3].id").value(4L))
                .andExpect(jsonPath("[4].id").value(5L))

                .andExpect(jsonPath("[0].name").value("item1"))
                .andExpect(jsonPath("[1].name").value("item2"))
                .andExpect(jsonPath("[2].name").value("item3"))
                .andExpect(jsonPath("[3].name").value("item4"))
                .andExpect(jsonPath("[4].name").value("item5"))

                .andExpect(jsonPath("[0].description").value("description1"))
                .andExpect(jsonPath("[1].description").value("description2"))
                .andExpect(jsonPath("[2].description").value("description3"))
                .andExpect(jsonPath("[3].description").value("description4"))
                .andExpect(jsonPath("[4].description").value("description5"))

                .andExpect(jsonPath("[0].owner.id").value(1L))
                .andExpect(jsonPath("[0].owner.name").value("user"))
                .andExpect(jsonPath("[0].owner.email").value("user@email.com"))

                .andExpect(jsonPath("[0].available").value(true))
                .andExpect(jsonPath("[1].available").value(true))
                .andExpect(jsonPath("[2].available").value(true))
                .andExpect(jsonPath("[3].available").value(true))
                .andExpect(jsonPath("[4].available").value(true))

                .andExpect(jsonPath("[0].comments").isEmpty())
                .andExpect(jsonPath("[1].comments").isEmpty())
                .andExpect(jsonPath("[2].comments").isEmpty())
                .andExpect(jsonPath("[3].comments").isEmpty())
                .andExpect(jsonPath("[4].comments").isEmpty());
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(any(), anyLong(), anyLong()))
                .thenReturn(new CommentOutDto(1L, "user", "text", LocalDateTime.now()));

        CommentIncDto comment = new CommentIncDto("comment");
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authorName").value("user"))
                .andExpect(jsonPath("$.text").value("text"));
    }

    private List<ItemWidthBookingsTimeDto> getItemsWidthBookingsTimeDto() {
        UserDto owner = getOwner();
        ItemWidthBookingsTimeDto item1 = new ItemWidthBookingsTimeDto(
                1L, "item1", "description1", owner, null,
                true, null, null, null, List.of());
        ItemWidthBookingsTimeDto item2 = new ItemWidthBookingsTimeDto(
                2L, "item2", "description2", owner, null,
                true, null, null, null, List.of());
        ItemWidthBookingsTimeDto item3 = new ItemWidthBookingsTimeDto(
                3L, "item3", "description3", owner, null,
                true, null, null, null, List.of());
        ItemWidthBookingsTimeDto item4 = new ItemWidthBookingsTimeDto(
                4L, "item4", "description4", owner, null,
                true, null, null, null, List.of());
        ItemWidthBookingsTimeDto item5 = new ItemWidthBookingsTimeDto(
                5L, "item5", "description5", owner, null,
                true, null, null, null, List.of());

        return List.of(item1, item2, item3, item4, item5);
    }

    private List<ItemOutDto> getItemsOutDto() {
        UserDto owner = getOwner();
        ItemOutDto item1 = new ItemOutDto(1L, "item1", "description1", owner,
                null, true, null, List.of());
        ItemOutDto item2 = new ItemOutDto(2L, "item2", "description2", owner,
                null, true, null, List.of());
        ItemOutDto item3 = new ItemOutDto(3L, "item3", "description3", owner,
                null, true, null, List.of());
        ItemOutDto item4 = new ItemOutDto(4L, "item4", "description4", owner,
                null, true, null, List.of());
        ItemOutDto item5 = new ItemOutDto(5L, "item5", "description5", owner,
                null, true, null, List.of());
        return List.of(item1, item2, item3, item4, item5);
    }

    private UserDto getOwner() {
        return new UserDto(1L, "user", "user@email.com");
    }
}
