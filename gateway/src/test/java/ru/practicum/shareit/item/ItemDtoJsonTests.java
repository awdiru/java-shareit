package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemIncDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoJsonTests {
    private final JacksonTester<ItemIncDto> jsonItemIncDto;
    private final JacksonTester<CommentDto> jsonCommentDto;

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
    void commentDtoTest() throws Exception {
        CommentDto commentDto = new CommentDto("text");
        JsonContent<CommentDto> result = jsonCommentDto.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }
}
