package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.model.dto.booking.BookingIncDto;
import ru.practicum.shareit.model.dto.booking.BookingOutDto;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.model.dto.booking.BookingWithItemsDto;
import ru.practicum.shareit.model.dto.comment.CommentIncDto;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.model.dto.item.ItemIncDto;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.dto.item.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.model.dto.request.RequestIncDto;
import ru.practicum.shareit.model.dto.request.RequestOutDto;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.model.dto.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.profiles.active=test")
@RequiredArgsConstructor
public class ItemServiceTests {
    private final ItemService itemService;
    private final UserService userService;
    private final RequestService requestService;
    private final BookingService bookingService;

    @Test
    void createItemTest() {
        UserDto owner = userService.createUser(
                new UserDto(null, "owner", "owner@email.com"));

        UserDto requestor = userService.createUser(
                new UserDto(null, "requestor", "requestor@email.com"));

        RequestOutDto request = requestService.createRequest(
                requestor.getId(), new RequestIncDto(null, "Request description"));

        ItemOutDto itemIs = itemService.createItem(
                new ItemIncDto("item", "description", true, request.getId()), owner.getId());
        ItemOutDto itemMust = new ItemOutDto(
                null, "item", "description", owner, null, true, request, null);

        Assertions.assertEquals(itemIs.getName(), itemMust.getName());
        Assertions.assertEquals(itemIs.getDescription(), itemMust.getDescription());
        Assertions.assertEquals(itemIs.getOwner().getName(), itemMust.getOwner().getName());
        Assertions.assertEquals(itemIs.getOwner().getEmail(), itemMust.getOwner().getEmail());
        Assertions.assertEquals(itemIs.getNumberOfRentals(), itemMust.getNumberOfRentals());
        Assertions.assertEquals(itemIs.getAvailable(), itemMust.getAvailable());
        Assertions.assertEquals(itemIs.getRequest().getDescription(), itemMust.getRequest().getDescription());
        Assertions.assertEquals(itemIs.getRequest().getCreated(), itemMust.getRequest().getCreated());
    }

    @Test
    void createItemWithIncorrectUserIdTest() {
        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> itemService.createItem(new ItemIncDto(), 1000L)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден.", e.getMessage());
    }

    @Test
    void createItemWithIncorrectRequestIdTest() {
        Long id = userService.createUser(new UserDto(null, "user", "user@email.com")).getId();

        IncorrectRequestIdException e = Assertions.assertThrows(
                IncorrectRequestIdException.class,
                () -> itemService.createItem(new ItemIncDto(
                        "item", "description", true, 1000L), id)
        );
        Assertions.assertEquals("Запрос с id 1000 не найден", e.getMessage());
    }

    @Test
    void updateItem() {
        UserDto owner = getOwner();


        Long itemId = itemService.createItem(
                new ItemIncDto("item", "description", true, null), owner.getId()
        ).getId();


        ItemOutDto itemIs = itemService.updateItem(
                itemId, new ItemIncDto("updItem", "updDescription", false, null), owner.getId());

        Assertions.assertEquals(itemIs.getName(), "updItem");
        Assertions.assertEquals(itemIs.getDescription(), "updDescription");
        Assertions.assertEquals(itemIs.getAvailable(), false);
        Assertions.assertNull(itemIs.getRequest());
    }

    @Test
    void updateItemWithIncorrectOwnerIdTest() {
        UserDto owner = getOwner();

        Long itemId = itemService.createItem(
                new ItemIncDto("item", "description", true, null), owner.getId()
        ).getId();

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> itemService.updateItem(itemId, new ItemIncDto(), owner.getId() + 1)
        );
        Assertions.assertEquals("Редактировать вещь может только ее владелец.", e.getMessage());
    }

    @Test
    void updateItemWithIncorrectItemIdTest() {
        UserDto owner = getOwner();

        Long itemId = itemService.createItem(
                new ItemIncDto("item", "description", true, null), owner.getId()
        ).getId() + 1;

        IncorrectItemIdException e = Assertions.assertThrows(
                IncorrectItemIdException.class,
                () -> itemService.updateItem(itemId, new ItemIncDto(), owner.getId())
        );
        Assertions.assertEquals("Вещь с id " + itemId + " не найдена.", e.getMessage());
    }

    @Test
    void getItem() {
        UserDto owner = getOwner();

        UserDto booker = userService.createUser(
                new UserDto(null, "booker", "booker@email.com"));

        Long itemId = itemService.createItem(
                new ItemIncDto("item", "description", true, null), owner.getId()
        ).getId();

        BookingOutDto booking1 = bookingService.createBooking(
                new BookingIncDto(itemId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)), booker.getId());
        bookingService.createBooking(
                new BookingIncDto(itemId, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)), booker.getId());


        ItemWidthBookingsTimeDto itemIs = itemService.getItem(itemId, owner.getId());
        BookingWithItemsDto booking = new BookingWithItemsDto(booking1.getId(), booking1.getStart(), booking1.getEnd(), booking1.getItem().getId(), booking1.getBooker().getId(), booking1.getStatus());

        Assertions.assertEquals(itemIs.getName(), "item");
        Assertions.assertEquals(itemIs.getDescription(), "description");
        Assertions.assertEquals(itemIs.getAvailable(), true);
        Assertions.assertEquals(itemIs.getNextBooking(), booking);
        Assertions.assertEquals(itemIs.getOwner(), owner);
        Assertions.assertEquals(itemIs.getComments(), List.of());

        Assertions.assertNull(itemIs.getNumberOfRentals());
        Assertions.assertNull(itemIs.getRequest());
        Assertions.assertNull(itemIs.getLastBooking());
    }

    @Test
    void getItemUser() {
        UserDto owner = getOwner();
        List<ItemWidthBookingsTimeDto> itemsIs = itemService.getItemsUser(owner.getId(), 0, 100);
        Assertions.assertEquals(itemsIs, List.of());

        Long itemId = itemService.createItem(new ItemIncDto("item1", "description1", true, null), owner.getId()).getId();
        ItemWidthBookingsTimeDto item1 = itemService.getItem(itemId, owner.getId());

        itemId = itemService.createItem(new ItemIncDto("item2", "description2", true, null), owner.getId()).getId();
        ItemWidthBookingsTimeDto item2 = itemService.getItem(itemId, owner.getId());

        itemId = itemService.createItem(new ItemIncDto("item3", "description3", true, null), owner.getId()).getId();
        ItemWidthBookingsTimeDto item3 = itemService.getItem(itemId, owner.getId());

        itemId = itemService.createItem(new ItemIncDto("item4", "description4", true, null), owner.getId()).getId();
        ItemWidthBookingsTimeDto item4 = itemService.getItem(itemId, owner.getId());

        List<ItemWidthBookingsTimeDto> itemsMust = List.of(item1, item2, item3, item4);
        itemsIs = itemService.getItemsUser(owner.getId(), 0, 100);
        Assertions.assertEquals(itemsIs, itemsMust);

        itemsMust = List.of(item1, item2);
        itemsIs = itemService.getItemsUser(owner.getId(), 0, 2);
        Assertions.assertEquals(itemsIs, itemsMust);

        itemsMust = List.of(item3, item4);
        itemsIs = itemService.getItemsUser(owner.getId(), 1, 2);
        Assertions.assertEquals(itemsIs, itemsMust);
    }

    @Test
    void searchItems() {
        UserDto owner = getOwner();
        ItemOutDto item1 = itemService.createItem(new ItemIncDto("item1one", "description1", true, null), owner.getId());
        item1.setComments(List.of());

        ItemOutDto item2 = itemService.createItem(new ItemIncDto("item2two", "description2", true, null), owner.getId());
        item2.setComments(List.of());

        ItemOutDto item3 = itemService.createItem(new ItemIncDto("ittem3three", "description3", true, null), owner.getId());
        item3.setComments(List.of());

        ItemOutDto item4 = itemService.createItem(new ItemIncDto("it4", "descriptionItem2", true, null), owner.getId());
        item4.setComments(List.of());

        ItemOutDto item5 = itemService.createItem(new ItemIncDto("it5", "descriptm2", true, null), owner.getId());
        item5.setComments(List.of());


        List<ItemOutDto> itemsIs = itemService.searchItems("item");
        List<ItemOutDto> itemsMust = List.of(item1, item2, item4);
        Assertions.assertEquals(itemsIs, itemsMust);

        itemsIs = itemService.searchItems("random text");
        Assertions.assertEquals(itemsIs, List.of());
    }

    @Test
    void addCommentTest() {
        UserDto owner = getOwner();

        ItemOutDto item = itemService.createItem(new ItemIncDto("item1one", "description1", true, null), owner.getId());

        ItemWidthBookingsTimeDto itemIs = itemService.getItem(item.getId(), owner.getId());
        Assertions.assertEquals(itemIs.getComments(), List.of());

        UserDto booker = userService.createUser(
                new UserDto(null, "booker", "booker@email.com"));
        BookingOutDto booking = bookingService.createBooking(new BookingIncDto(item.getId(), LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)), booker.getId());
        bookingService.approvedBooking(owner.getId(), booking.getId(), true);

        CommentOutDto commentIs1 = itemService.addComment(new CommentIncDto("comment"), item.getId(), booker.getId());
        itemIs = itemService.getItem(item.getId(), owner.getId());
        Assertions.assertEquals(itemIs.getComments(), List.of(commentIs1));

        CommentOutDto commentIs2 = itemService.addComment(new CommentIncDto("comment2"), item.getId(), booker.getId());
        itemIs = itemService.getItem(item.getId(), owner.getId());
        Assertions.assertEquals(itemIs.getComments(), List.of(commentIs1, commentIs2));
    }

    private UserDto getOwner() {
        return userService.createUser(new UserDto(null, "owner", "owner@email.com"));
    }
}