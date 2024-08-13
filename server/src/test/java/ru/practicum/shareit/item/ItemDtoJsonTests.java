package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.item.dto.model.comment.CommentIncDto;
import ru.practicum.shareit.item.dto.model.comment.CommentOutDto;
import ru.practicum.shareit.item.dto.model.item.ItemIncDto;
import ru.practicum.shareit.item.dto.model.item.ItemOutDto;
import ru.practicum.shareit.item.dto.model.item.ItemToRequestDto;
import ru.practicum.shareit.item.dto.model.item.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor
public class ItemDtoJsonTests {
    private final JacksonTester<ItemIncDto> jsonItemIncDto;
    private final JacksonTester<ItemOutDto> jsonItemOutDto;
    private final JacksonTester<ItemToRequestDto> jsonItemToRequestDto;
    private final JacksonTester<ItemWidthBookingsTimeDto> jsonItemWidthBookingsTimeDto;

    private final JacksonTester<CommentIncDto> jsonCommentIncDto;
    private final JacksonTester<CommentOutDto> jsonCommentOutDto;

    @Test
    void itemIncDtoTest() throws Exception {
        ItemIncDto itemIncDto = new ItemIncDto("item", "description", true, 1L);
        JsonContent<ItemIncDto> result = jsonItemIncDto.write(itemIncDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void itemOutDtoTest() throws Exception {
        ItemOutDto itemOutDto = getItemOutDto();
        JsonContent<ItemOutDto> result = jsonItemOutDto.write(itemOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.numberOfRentals").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("owner");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("owner@email.com");

        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.description").isEqualTo("description");

        assertThat(result).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments.[1].id").isEqualTo(2);

        assertThat(result).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("user1");
        assertThat(result).extractingJsonPathStringValue("$.comments.[1].authorName").isEqualTo("user2");

        assertThat(result).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("text1");
        assertThat(result).extractingJsonPathStringValue("$.comments.[1].text").isEqualTo("text2");
    }

    @Test
    void itemWidthBookingsTimeDtoTest() throws Exception {
        ItemWidthBookingsTimeDto itemWidthBookingsTimeDto = getItemWidthBookingsTimeDto();
        JsonContent<ItemWidthBookingsTimeDto> result = jsonItemWidthBookingsTimeDto.write(itemWidthBookingsTimeDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.numberOfRentals").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("owner");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("owner@email.com");

        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.description").isEqualTo("description");

        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.status").isEqualTo("APPROVED");

        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.status").isEqualTo("APPROVED");

        assertThat(result).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments.[1].id").isEqualTo(2);

        assertThat(result).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("user1");
        assertThat(result).extractingJsonPathStringValue("$.comments.[1].authorName").isEqualTo("user2");

        assertThat(result).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("text1");
        assertThat(result).extractingJsonPathStringValue("$.comments.[1].text").isEqualTo("text2");

    }

    private ItemOutDto getItemOutDto() {
        UserDto owner = new UserDto(1L, "owner", "owner@email.com");
        RequestOutDto request = new RequestOutDto(1L, "description", LocalDateTime.now());

        CommentOutDto comment1 = new CommentOutDto(1L, "user1", "text1", LocalDateTime.now().minusDays(1));
        CommentOutDto comment2 = new CommentOutDto(2L, "user2", "text2", LocalDateTime.now().minusDays(2));
        List<CommentOutDto> comments = List.of(comment1, comment2);

        return new ItemOutDto(1L, "item", "description", owner,
                1, true, request, comments);
    }

    private ItemWidthBookingsTimeDto getItemWidthBookingsTimeDto() {
        UserDto owner = new UserDto(1L, "owner", "owner@email.com");
        RequestOutDto request = new RequestOutDto(1L, "description", LocalDateTime.now());

        CommentOutDto comment1 = new CommentOutDto(1L, "user1", "text1", LocalDateTime.now().minusDays(1));
        CommentOutDto comment2 = new CommentOutDto(2L, "user2", "text2", LocalDateTime.now().minusDays(2));
        List<CommentOutDto> comments = List.of(comment1, comment2);

        BookingWithItemsDto lastBooking = new BookingWithItemsDto(1L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), 1L, 1L, BookingStatusEnum.APPROVED);
        BookingWithItemsDto nextBooking = new BookingWithItemsDto(2L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, 2L, BookingStatusEnum.APPROVED);

        return new ItemWidthBookingsTimeDto(1L, "item", "description", owner, 1,
                true, request, lastBooking, nextBooking, comments);
    }


    @Test
    void commentIntDtoTest() throws Exception {
        CommentIncDto commentIncDto = new CommentIncDto("text");
        JsonContent<CommentIncDto> result = jsonCommentIncDto.write(commentIncDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }

    @Test
    void commentOutDtoTest() throws Exception {
        CommentOutDto commentOutDto = new CommentOutDto(1L, "author", "text", LocalDateTime.now());
        JsonContent<CommentOutDto> result = jsonCommentOutDto.write(commentOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.created");
    }
}