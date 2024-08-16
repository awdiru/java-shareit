package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.model.dto.booking.BookingIncDto;
import ru.practicum.shareit.model.dto.booking.BookingOutDto;
import ru.practicum.shareit.model.enums.BookingStatusEnum;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.dto.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingService bookingService;

    private final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(2);
    private final String userIdHead = "X-Sharer-User-Id";

    @Test
    void createBookingTest() throws Exception {
        UserDto owner = createUser("owner@email.com");
        UserDto booker = createUser("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner);
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(new BookingOutDto(1L, start, end, itemOutDto, booker, BookingStatusEnum.WAITING));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())

                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.booker.name").value(booker.getName()))
                .andExpect(jsonPath("$.booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("$.item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("$.item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("$.item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("$.item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("$.item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("$.item.comments").isEmpty())

                .andExpect(jsonPath("$.item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("$.item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("$.item.owner.email").value(itemOutDto.getOwner().getEmail()));
    }

    @Test
    void createBookingWithIncorrectUserIdTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void createBookingWithIncorrectItemIdTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenThrow(new IncorrectItemIdException("Предмет с id 1 не найден"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Предмет с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void createBookingWithIncorrectItemAvailableTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenThrow(new ItemAvailableException("Предмет с id 1 не доступен для бронирования"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Предмет с id 1 не доступен для бронирования"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void createBookingWithIncorrectBookingTimeTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenThrow(new IncorrectBookingTimeException(
                        "Время окончания бронирования не может быть раньше или равно времени начала бронирования"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Время окончания бронирования не может быть раньше или равно времени начала бронирования"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void createBookingWithIncorrectBookerTest() throws Exception {
        when(bookingService.createBooking(any(), anyLong()))
                .thenThrow(new FailCreateBookingOwnerItem("Владелец вещи не может ее забронировать"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Владелец вещи не может ее забронировать"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void approvedBookingTest() throws Exception {
        UserDto owner = createUser("owner@email.com");
        UserDto booker = createUser("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner);
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new BookingOutDto(1L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())

                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.booker.name").value(booker.getName()))
                .andExpect(jsonPath("$.booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("$.item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("$.item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("$.item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("$.item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("$.item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("$.item.comments").isEmpty())

                .andExpect(jsonPath("$.item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("$.item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("$.item.owner.email").value(itemOutDto.getOwner().getEmail()));
    }

    @Test
    void approvedBookingWithIncorrectBookingIdTest() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new IncorrectBookingIdException("Бронирование с id 1 не найдено"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Бронирование с id 1 не найдено"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void approvedBookingWithIncorrectOwnerIdTest() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new IncorrectOwnerIdException("Бронирование может подтвердить только владелец вещи"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Бронирование может подтвердить только владелец вещи"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void approvedBookingWithIncorrectUserIdTest() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new IncorrectUserIdException("Бронирование может подтвердить только владелец вещи"));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Бронирование может подтвердить только владелец вещи"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void approvedBookingWithFailApprovedBookingTest() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new FailApprovedBookingException("Подтверждение бронирования уже произошло. Статус бронирования: " + BookingStatusEnum.APPROVED));

        BookingIncDto bookingIncDto = new BookingIncDto(null, start, end);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingIncDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Подтверждение бронирования уже произошло. Статус бронирования: " + BookingStatusEnum.APPROVED))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void getBookingTest() throws Exception {
        UserDto owner = createUser("owner@email.com");
        UserDto booker = createUser("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner);

        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(new BookingOutDto(1L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED));

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())

                .andExpect(jsonPath("$.booker.id").value(booker.getId()))
                .andExpect(jsonPath("$.booker.name").value(booker.getName()))
                .andExpect(jsonPath("$.booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("$.item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("$.item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("$.item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("$.item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("$.item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("$.item.comments").isEmpty())

                .andExpect(jsonPath("$.item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("$.item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("$.item.owner.email").value(itemOutDto.getOwner().getEmail()));
    }

    @Test
    void getBookingWithIncorrectBookingIdTest() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenThrow(new IncorrectBookingIdException("Бронирование с id 1 не найдено"));

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Бронирование с id 1 не найдено"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void getBookingWithIncorrectUserIdTest() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenThrow(new IncorrectUserIdException("Получение данных о конкретном бронировании " +
                        "может быть выполнено либо автором бронирования, либо владельцем вещи"));

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Получение данных о конкретном бронировании " +
                        "может быть выполнено либо автором бронирования, либо владельцем вещи"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void getAllBookingsUserTest() throws Exception {
        UserDto owner = createUser("owner@email.com");
        UserDto booker = createUser("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner);

        BookingOutDto booking1 = new BookingOutDto(1L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED);
        BookingOutDto booking2 = new BookingOutDto(2L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED);

        List<BookingOutDto> bookings = List.of(booking1, booking2);
        when(bookingService.getAllBookingsUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings?state=all&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1L))
                .andExpect(jsonPath("[0].status").value("APPROVED"))
                .andExpect(jsonPath("[0].start").isNotEmpty())
                .andExpect(jsonPath("[0].end").isNotEmpty())

                .andExpect(jsonPath("[0].booker.id").value(booker.getId()))
                .andExpect(jsonPath("[0].booker.name").value(booker.getName()))
                .andExpect(jsonPath("[0].booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("[0].item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("[0].item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("[0].item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("[0].item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("[0].item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("[0].item.comments").isEmpty())

                .andExpect(jsonPath("[0].item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("[0].item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("[0].item.owner.email").value(itemOutDto.getOwner().getEmail()))

                .andExpect(jsonPath("[1].id").value(2L))
                .andExpect(jsonPath("[1].status").value("APPROVED"))
                .andExpect(jsonPath("[1].start").isNotEmpty())
                .andExpect(jsonPath("[1].end").isNotEmpty())

                .andExpect(jsonPath("[1].booker.id").value(booker.getId()))
                .andExpect(jsonPath("[1].booker.name").value(booker.getName()))
                .andExpect(jsonPath("[1].booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("[1].item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("[1].item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("[1].item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("[1].item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("[1].item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("[1].item.comments").isEmpty())

                .andExpect(jsonPath("[1].item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("[1].item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("[1].item.owner.email").value(itemOutDto.getOwner().getEmail()));
    }

    @Test
    void getAllBookingsUserWithIncorrectUserIdTest() throws Exception {
        when(bookingService.getAllBookingsUser(anyLong(), any(), anyInt(), anyInt()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден"));

        mvc.perform(get("/bookings?state=all&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    @Test
    void getAllBookingsItemsUserTest() throws Exception {
        UserDto owner = createUser("owner@email.com");
        UserDto booker = createUser("booker@email.com");
        ItemOutDto itemOutDto = createItemOutDto(owner);

        BookingOutDto booking1 = new BookingOutDto(1L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED);
        BookingOutDto booking2 = new BookingOutDto(2L, start, end, itemOutDto, booker, BookingStatusEnum.APPROVED);

        List<BookingOutDto> bookings = List.of(booking1, booking2);
        when(bookingService.getAllBookingsItemsUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner?state=all&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1L))
                .andExpect(jsonPath("[0].status").value("APPROVED"))
                .andExpect(jsonPath("[0].start").isNotEmpty())
                .andExpect(jsonPath("[0].end").isNotEmpty())

                .andExpect(jsonPath("[0].booker.id").value(booker.getId()))
                .andExpect(jsonPath("[0].booker.name").value(booker.getName()))
                .andExpect(jsonPath("[0].booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("[0].item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("[0].item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("[0].item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("[0].item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("[0].item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("[0].item.comments").isEmpty())

                .andExpect(jsonPath("[0].item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("[0].item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("[0].item.owner.email").value(itemOutDto.getOwner().getEmail()))

                .andExpect(jsonPath("[1].id").value(2L))
                .andExpect(jsonPath("[1].status").value("APPROVED"))
                .andExpect(jsonPath("[1].start").isNotEmpty())
                .andExpect(jsonPath("[1].end").isNotEmpty())

                .andExpect(jsonPath("[1].booker.id").value(booker.getId()))
                .andExpect(jsonPath("[1].booker.name").value(booker.getName()))
                .andExpect(jsonPath("[1].booker.email").value(booker.getEmail()))

                .andExpect(jsonPath("[1].item.id").value(itemOutDto.getId()))
                .andExpect(jsonPath("[1].item.name").value(itemOutDto.getName()))
                .andExpect(jsonPath("[1].item.description").value(itemOutDto.getDescription()))
                .andExpect(jsonPath("[1].item.numberOfRentals").value(itemOutDto.getNumberOfRentals()))
                .andExpect(jsonPath("[1].item.available").value(itemOutDto.getAvailable()))
                .andExpect(jsonPath("[1].item.comments").isEmpty())

                .andExpect(jsonPath("[1].item.owner.id").value(itemOutDto.getOwner().getId()))
                .andExpect(jsonPath("[1].item.owner.name").value(itemOutDto.getOwner().getName()))
                .andExpect(jsonPath("[1].item.owner.email").value(itemOutDto.getOwner().getEmail()));
    }

    @Test
    void getAllBookingsItemsUserWithIncorrectUserIdTest() throws Exception {
        when(bookingService.getAllBookingsItemsUser(anyLong(), any(), anyInt(), anyInt()))
                .thenThrow(new IncorrectUserIdException("Пользователь с id 1 не найден"));

        mvc.perform(get("/bookings/owner?state=all&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(userIdHead, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Пользователь с id 1 не найден"))
                .andExpect(jsonPath("$.path").value("/bookings"));
    }

    private UserDto createUser(String email) {
        return new UserDto(1L, "user", email);
    }

    private ItemOutDto createItemOutDto(UserDto owner) {
        return new ItemOutDto(1L, "item", "description", owner, 1, true, null, List.of());
    }
}
