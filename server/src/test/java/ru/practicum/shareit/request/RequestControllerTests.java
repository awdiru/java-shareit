package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.model.dto.request.RequestIncDto;
import ru.practicum.shareit.model.dto.request.RequestOutDto;
import ru.practicum.shareit.model.dto.request.RequestWithItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    RequestService requestService;
    private final String userIdHead = "X-Sharer-User-Id";

    @Test
    void createRequestTest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(new RequestOutDto(1L, "description", LocalDateTime.now()));

        RequestIncDto requestIncDto = new RequestIncDto(null, "description");
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }

    @Test
    void createRequestWithIncorrectUserIdTest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        RequestIncDto requestIncDto = new RequestIncDto(null, "description");
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/requests"));
    }

    @Test
    void getRequestsUserTest() throws Exception {
        RequestWithItemDto requestWithItemDto1 = new RequestWithItemDto(1L, "description1", LocalDateTime.now(), List.of());
        RequestWithItemDto requestWithItemDto2 = new RequestWithItemDto(2L, "description2", LocalDateTime.now(), List.of());
        RequestWithItemDto requestWithItemDto3 = new RequestWithItemDto(3L, "description3", LocalDateTime.now(), List.of());
        List<RequestWithItemDto> requests = List.of(requestWithItemDto1, requestWithItemDto2, requestWithItemDto3);

        when(requestService.getRequestsUser(anyLong()))
                .thenReturn(requests);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[1].id").value(2))
                .andExpect(jsonPath("[2].id").value(3))
                .andExpect(jsonPath("[0].description").value("description1"))
                .andExpect(jsonPath("[1].description").value("description2"))
                .andExpect(jsonPath("[2].description").value("description3"));
    }

    @Test
    void getRequestsUserWithIncorrectUserIdTest() throws Exception {
        when(requestService.getRequestsUser(anyLong()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/requests"));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        RequestOutDto request1 = new RequestOutDto(1L, "description1", LocalDateTime.now());
        RequestOutDto request2 = new RequestOutDto(2L, "description2", LocalDateTime.now());
        RequestOutDto request3 = new RequestOutDto(3L, "description3", LocalDateTime.now());
        List<RequestOutDto> requests = List.of(request1, request2, request3);

        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(requests);

        mvc.perform(get("/requests/all?from=0&size=100")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[1].id").value(2))
                .andExpect(jsonPath("[2].id").value(3))
                .andExpect(jsonPath("[0].description").value("description1"))
                .andExpect(jsonPath("[1].description").value("description2"))
                .andExpect(jsonPath("[2].description").value("description3"));
    }

    @Test
    void getAllRequestsWithIncorrectUserId() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден."));

        mvc.perform(get("/requests/all?from=0&size=100")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден."))
                .andExpect(jsonPath("$.path").value("/requests"));
    }

    @Test
    void getRequestTest() throws Exception {
        when(requestService.getRequest(anyLong()))
                .thenReturn(new RequestWithItemDto(1L, "description", LocalDateTime.now(), List.of()));

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void getRequestWithIncorrectRequestIdTest() throws Exception {
        when(requestService.getRequest(anyLong()))
                .thenThrow(new IncorrectRequestIdException("Запрос с id 1 не найден"));

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Запрос с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/requests"));
    }
}
