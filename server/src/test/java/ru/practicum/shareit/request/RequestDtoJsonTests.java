package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemToRequestDto;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor
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
        List<ItemToRequestDto> items = List.of();

        RequestWithItemDto requestOutDto = new RequestWithItemDto(1L, "description", LocalDateTime.now(), items);
        JsonContent<RequestWithItemDto> result = jsonRequestWithItemDto.write(requestOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.created");

        assertThat(result).hasEmptyJsonPathValue("$.items");
    }
}