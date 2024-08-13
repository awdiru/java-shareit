package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.item.dto.model.item.ItemOutDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor
public class BookingDtoJsonTests {
    private final JacksonTester<BookingIncDto> jsonBookingIncDto;
    private final JacksonTester<BookingOutDto> jsonBookingOutDto;
    private final JacksonTester<BookingWithItemsDto> jsonBookingWithItemsDto;

    LocalDateTime start = LocalDateTime.now().plusDays(1);
    LocalDateTime end = LocalDateTime.now().plusDays(2);

    @Test
    void bookingIncDtoTest() throws Exception {
        BookingIncDto bookingIncDto = new BookingIncDto(1L, start, end);
        JsonContent<BookingIncDto> result = jsonBookingIncDto.write(bookingIncDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
    }

    @Test
    void bookingOutDtoTest() throws Exception {
        UserDto owner = new UserDto(1L, "owner", "owner@email.com");
        UserDto booker = new UserDto(2L, "booker", "booker@email.com");
        ItemOutDto item = new ItemOutDto(1L, "name", "description", owner, 1, true, null, List.of());
        BookingOutDto bookingOutDto = new BookingOutDto(1L, start, end, item, booker, BookingStatusEnum.APPROVED);

        JsonContent<BookingOutDto> result = jsonBookingOutDto.write(bookingOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.item.numberOfRentals").isEqualTo(item.getNumberOfRentals());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable());

        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo(item.getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo(item.getOwner().getEmail());
    }

    @Test
    void bookingWithItemsDtoTest() throws Exception {
        BookingWithItemsDto booking = new BookingWithItemsDto(1L, start, end, 1L, 1L, BookingStatusEnum.APPROVED);

        JsonContent<BookingWithItemsDto> result = jsonBookingWithItemsDto.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}
