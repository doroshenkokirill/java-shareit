package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void test() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .available(true)
                .requestId(1)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.requestId");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(itemDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(itemDto.getName()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(itemDto.getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(item_available -> assertThat(item_available).isEqualTo(itemDto.getAvailable()));
    }
}