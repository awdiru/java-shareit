package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.model.ItemIncDto;
import ru.practicum.shareit.item.dto.model.ItemOutDto;
import ru.practicum.shareit.item.dto.model.ItemToRequestDto;
import ru.practicum.shareit.request.dto.model.RequestIncDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.request.dto.model.RequestWithItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTests {
    private final RequestService requestService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void createRequestTest() {
        UserDto requestor = userService.createUser(new UserDto(null, "requestor", "requestor@email.com"));
        RequestOutDto requestOutDto = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));

        Assertions.assertEquals(requestOutDto.getDescription(), "description");
        Assertions.assertNotNull(requestOutDto.getId());
        Assertions.assertTrue(requestOutDto.getCreated().isBefore(LocalDateTime.now()));
    }

    @Test
    void createRequestWithIncorrectUserIdTest() {
        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> requestService.createRequest(1000L, new RequestIncDto())
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e.getMessage());
    }

    @Test
    void getRequestsUserTest() {
        UserDto requestor = userService.createUser(new UserDto(null, "requestor", "requestor@email.com"));
        RequestOutDto requestOutDto1 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));
        RequestWithItemDto requestWithItemDto1 = requestService.getRequest(requestOutDto1.getId());

        RequestOutDto requestOutDto2 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));
        RequestWithItemDto requestWithItemDto2 = requestService.getRequest(requestOutDto2.getId());

        RequestOutDto requestOutDto3 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));
        RequestWithItemDto requestWithItemDto3 = requestService.getRequest(requestOutDto3.getId());

        List<RequestWithItemDto> requestsMust = List.of(requestWithItemDto3, requestWithItemDto2, requestWithItemDto1);
        List<RequestWithItemDto> requestsIs = requestService.getRequestsUser(requestor.getId());

        Assertions.assertEquals(requestsIs, requestsMust);
    }

    @Test
    void getRequestsUserWithIncorrectUserIdTest() {
        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> requestService.getRequestsUser(1000L)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e.getMessage());
    }

    @Test
    void getAllRequests() {
        UserDto requestor = userService.createUser(new UserDto(null, "requestor", "requestor@email.com"));
        UserDto requestor2 = userService.createUser(new UserDto(null, "requestor2", "requestor2@email.com"));

        RequestOutDto requestOutDto1 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));

        RequestOutDto requestOutDto2 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));

        RequestOutDto requestOutDto3 = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));

        requestService.createRequest(requestor2.getId(), new RequestIncDto(null, "description"));
        requestService.createRequest(requestor2.getId(), new RequestIncDto(null, "description"));

        List<RequestOutDto> requestsMust = List.of(requestOutDto1, requestOutDto2, requestOutDto3);
        List<RequestOutDto> requestsIs = requestService.getAllRequests(requestor2.getId(), 0, 10);

        Assertions.assertEquals(requestsIs, requestsMust);
    }

    @Test
    void getAllRequestsWithIncorrectUserIdTest() {
        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> requestService.getAllRequests(1000L, 0, 100)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e.getMessage());
    }

    @Test
    void getRequestTest() {
        UserDto owner = userService.createUser(new UserDto(null, "owner", "owner@email.com"));
        UserDto requestor = userService.createUser(new UserDto(null, "requestor", "requestor@email.com"));
        RequestOutDto requestOutDto = requestService
                .createRequest(requestor.getId(), new RequestIncDto(null, "description"));

        ItemOutDto it1 = itemService.createItem(new ItemIncDto("item1", "description", true, requestOutDto.getId()), owner.getId());
        ItemToRequestDto item1 = new ItemToRequestDto(it1.getId(), it1.getName(), it1.getOwner().getId());

        ItemOutDto it2 = itemService.createItem(new ItemIncDto("item2", "description", true, requestOutDto.getId()), owner.getId());
        ItemToRequestDto item2 = new ItemToRequestDto(it2.getId(), it2.getName(), it2.getOwner().getId());

        ItemOutDto it3 = itemService.createItem(new ItemIncDto("item3", "description", true, requestOutDto.getId()), owner.getId());
        ItemToRequestDto item3 = new ItemToRequestDto(it3.getId(), it3.getName(), it3.getOwner().getId());

        RequestWithItemDto requestWithItemDto = requestService.getRequest(requestOutDto.getId());

        Assertions.assertNotNull(requestWithItemDto.getId());
        Assertions.assertEquals(requestWithItemDto.getDescription(), "description");
        Assertions.assertTrue(requestWithItemDto.getCreated().isBefore(LocalDateTime.now()));
        Assertions.assertEquals(requestWithItemDto.getItems(), List.of(item1, item2, item3));
    }
}
