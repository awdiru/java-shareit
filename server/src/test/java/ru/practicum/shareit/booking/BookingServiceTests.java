package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.enums.BookingStatusEnum;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemIncDto;
import ru.practicum.shareit.item.ItemOutDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.profiles.active=test")
@RequiredArgsConstructor
public class BookingServiceTests {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(2);

    @Test
    void createBookingTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto bookingIs = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());
        BookingOutDto bookingMust = new BookingOutDto(null, start, end, itemOutDto, booker, BookingStatusEnum.WAITING);

        Assertions.assertEquals(bookingIs.getStart(), bookingMust.getStart());
        Assertions.assertEquals(bookingIs.getEnd(), bookingMust.getEnd());
        Assertions.assertEquals(bookingIs.getItem(), bookingMust.getItem());
        Assertions.assertEquals(bookingIs.getBooker(), bookingMust.getBooker());
        Assertions.assertEquals(bookingIs.getStatus(), bookingMust.getStatus());
    }

    @Test
    void createBookingWithIncorrectDateTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        IncorrectItemIdException e1 = Assertions.assertThrows(
                IncorrectItemIdException.class,
                () -> bookingService.createBooking(new BookingIncDto(1000L, start, end), booker.getId())
        );
        Assertions.assertEquals("Предмет с id 1000 не найден", e1.getMessage());

        IncorrectUserIdException e2 = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> bookingService.createBooking(new BookingIncDto(itemOutDto.getId(), start, end), 1000L)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e2.getMessage());

        IncorrectBookingTimeException e3 = Assertions.assertThrows(
                IncorrectBookingTimeException.class,
                () -> bookingService.createBooking(new BookingIncDto(itemOutDto.getId(), start.plusDays(3), end), booker.getId())
        );
        Assertions.assertEquals("Время окончания бронирования не может быть раньше или равно времени начала бронирования", e3.getMessage());

        FailCreateBookingOwnerItem e4 = Assertions.assertThrows(
                FailCreateBookingOwnerItem.class,
                () -> bookingService.createBooking(new BookingIncDto(itemOutDto.getId(), start, end), owner.getId())
        );
        Assertions.assertEquals("Владелец вещи не может ее забронировать", e4.getMessage());

        itemService.updateItem(itemOutDto.getId(), new ItemIncDto(null, null, false, null), owner.getId());

        ItemAvailableException e5 = Assertions.assertThrows(
                ItemAvailableException.class,
                () -> bookingService.createBooking(new BookingIncDto(itemOutDto.getId(), start, end), booker.getId())
        );
        Assertions.assertEquals("Предмет с id " + itemOutDto.getId() + " не доступен для бронирования", e5.getMessage());
    }

    @Test
    void approvedBookingTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto booking = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());
        itemOutDto.setNumberOfRentals(1);

        BookingOutDto bookingIs = bookingService.approvedBooking(owner.getId(), booking.getId(), true);
        BookingOutDto bookingMust = new BookingOutDto(null, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED);

        Assertions.assertEquals(bookingIs.getStart(), bookingMust.getStart());
        Assertions.assertEquals(bookingIs.getEnd(), bookingMust.getEnd());
        Assertions.assertEquals(bookingIs.getItem(), bookingMust.getItem());
        Assertions.assertEquals(bookingIs.getBooker(), bookingMust.getBooker());
        Assertions.assertEquals(bookingIs.getStatus(), bookingMust.getStatus());
    }

    @Test
    void approvedBookingWithIncorrectDateTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto booking = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());

        IncorrectOwnerIdException e = Assertions.assertThrows(
                IncorrectOwnerIdException.class,
                () -> bookingService.approvedBooking(1000L, booking.getId(), true)
        );
        Assertions.assertEquals("Бронирование может подтвердить только владелец вещи", e.getMessage());

        IncorrectUserIdException e1 = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> bookingService.approvedBooking(booker.getId(), booking.getId(), true)
        );
        Assertions.assertEquals("Бронирование может подтвердить только владелец вещи", e1.getMessage());

        bookingService.approvedBooking(owner.getId(), booking.getId(), true);

        FailApprovedBookingException e2 = Assertions.assertThrows(
                FailApprovedBookingException.class,
                () -> bookingService.approvedBooking(owner.getId(), booking.getId(), true)
        );
        Assertions.assertEquals("Подтверждение бронирования уже произошло. Статус бронирования: " + BookingStatusEnum.APPROVED, e2.getMessage());
    }

    @Test
    void getBooking() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto booking = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());
        itemOutDto.setNumberOfRentals(1);

        BookingOutDto bookingIsBooker = bookingService.getBooking(booker.getId(), booking.getId());
        Assertions.assertEquals(bookingIsBooker, booking);

        BookingOutDto bookingIsOwner = bookingService.getBooking(owner.getId(), booking.getId());
        Assertions.assertEquals(bookingIsOwner, booking);
    }

    @Test
    void getBookingWithIncorrectDateTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto booking = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());
        itemOutDto.setNumberOfRentals(1);

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> bookingService.getBooking(1000L, booking.getId())
        );
        Assertions.assertEquals("Получение данных о конкретном бронировании " +
                "может быть выполнено либо автором бронирования, либо владельцем вещи", e.getMessage());

        IncorrectBookingIdException e1 = Assertions.assertThrows(
                IncorrectBookingIdException.class,
                () -> bookingService.getBooking(booker.getId(), 1000L)
        );
        Assertions.assertEquals("Бронирование с id 1000 не найдено", e1.getMessage());
    }

    @Test
    void getAllBookingsUserTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner.getId());

        BookingOutDto booking1 = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start, end), booker.getId());

        BookingOutDto booking2 = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start.minusDays(5), end.minusDays(4)), booker.getId());

        BookingOutDto booking3 = bookingService.createBooking(
                new BookingIncDto(itemOutDto.getId(), start.minusDays(5), end.plusDays(3)), booker.getId());

        List<BookingOutDto> bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "ALL", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1, booking2, booking3));

        bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "CURRENT", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking3));

        bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "PAST", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking2));

        bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "FUTURE", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1));

        booking1 = bookingService.approvedBooking(owner.getId(), booking1.getId(), false);

        bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "REJECTED", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1));

        bookingsIs = bookingService.getAllBookingsUser(booker.getId(), "WAITING", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking2, booking3));

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> bookingService.getAllBookingsUser(1000L, "ALL", 0, 10)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e.getMessage());
    }

    @Test
    void getAllBookingsItemsUserTest() {
        UserDto owner = createUserDto("owner@email.com");
        UserDto booker = createUserDto("booker@email.com");

        ItemOutDto itemOutDto1 = createItemOutDto(owner.getId());
        ItemOutDto itemOutDto2 = createItemOutDto(owner.getId());
        ItemOutDto itemOutDto3 = createItemOutDto(owner.getId());

        BookingOutDto booking1 = bookingService.createBooking(
                new BookingIncDto(itemOutDto1.getId(), start, end), booker.getId());

        BookingOutDto booking2 = bookingService.createBooking(
                new BookingIncDto(itemOutDto2.getId(), start.minusDays(5), end.minusDays(4)), booker.getId());

        BookingOutDto booking3 = bookingService.createBooking(
                new BookingIncDto(itemOutDto3.getId(), start.minusDays(5), end.plusDays(3)), booker.getId());

        List<BookingOutDto> bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "ALL", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1, booking2, booking3));

        bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "CURRENT", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking3));

        bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "PAST", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking2));

        bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "FUTURE", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1));

        booking1 = bookingService.approvedBooking(owner.getId(), booking1.getId(), false);

        bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "REJECTED", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking1));

        bookingsIs = bookingService.getAllBookingsItemsUser(owner.getId(), "WAITING", 0, 10);
        Assertions.assertEquals(bookingsIs, List.of(booking2, booking3));

        IncorrectUserIdException e = Assertions.assertThrows(
                IncorrectUserIdException.class,
                () -> bookingService.getAllBookingsItemsUser(1000L, "ALL", 0, 10)
        );
        Assertions.assertEquals("Пользователь с id 1000 не найден", e.getMessage());
    }

    private UserDto createUserDto(String email) {
        return userService.createUser(new UserDto(null, "user", email));
    }

    private ItemOutDto createItemOutDto(Long userId) {
        return itemService.createItem(new ItemIncDto("item", "description", true, null), userId);
    }
}
