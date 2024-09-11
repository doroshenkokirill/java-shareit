package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    @SneakyThrows
    public void test() {
        User user = User.builder()
                .id(1).name("Name").email("Email@email.com")
                .build();
        List<ItemDto> itemDtoList = new ArrayList<>();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("Description")
                .requester(user)
                .created(LocalDateTime.now())
                .items(itemDtoList)
                .build();
        JsonContent<ItemRequestDto> content = json.write(itemRequestDto);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requester")
                .hasJsonPath("$.items")
                .hasJsonPath("$.created");

        assertThat(content).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("@.description").isEqualTo("Description");
        assertThat(content).extractingJsonPathStringValue("@.requester.name").isEqualTo("Name");
    }
}