package ru.practicum.shareit.comment.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;
    @Test
    void test() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("Text")
                .created(LocalDateTime.now())
                .build();

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.text")
                .hasJsonPath("$.item")
                .hasJsonPath("$.author")
                .hasJsonPath("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(item_id -> assertThat(item_id.longValue()).isEqualTo(commentDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(commentDto.getText()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isNotNull());
    }
}