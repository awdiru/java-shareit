package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.model.ItemToRequestDto;
import ru.practicum.shareit.request.dto.model.RequestIncDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.request.dto.model.RequestWithItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoJsonTests {
    private final JacksonTester<RequestIncDto> jsonRequestIncDto;
    private final JacksonTester<RequestOutDto> jsonRequestOutDto;
    private final JacksonTester<RequestWithItemDto> jsonRequestWithItemDto;


    @Test
    void requestIncDtoTest() throws Exception {
        RequestIncDto requestIncDto = new RequestIncDto(1L, "description");
        JsonContent<RequestIncDto> result = jsonRequestIncDto.write(requestIncDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    void requestOutDtoTest() throws Exception {
        RequestOutDto requestOutDto = new RequestOutDto(1L, "description", LocalDateTime.now());
        JsonContent<RequestOutDto> result = jsonRequestOutDto.write(requestOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.created");
    }

    @Test
    void requestWithItemDtoTest() throws Exception {
        ItemToRequestDto item1 = new ItemToRequestDto(1L, "item1", 1L);
        ItemToRequestDto item2 = new ItemToRequestDto(2L, "item2", 2L);
        ItemToRequestDto item3 = new ItemToRequestDto(3L, "item3", 3L);
        List<ItemToRequestDto> items = List.of(item1, item2, item3);

        RequestWithItemDto requestOutDto = new RequestWithItemDto(1L, "description", LocalDateTime.now(), items);
        JsonContent<RequestWithItemDto> result = jsonRequestWithItemDto.write(requestOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.items.[1].id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.items.[2].id").isEqualTo(3);

        assertThat(result).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("item1");
        assertThat(result).extractingJsonPathStringValue("$.items.[1].name").isEqualTo("item2");
        assertThat(result).extractingJsonPathStringValue("$.items.[2].name").isEqualTo("item3");

        assertThat(result).extractingJsonPathNumberValue("$.items.[0].owner").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.items.[1].owner").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.items.[2].owner").isEqualTo(3);
    }
}
