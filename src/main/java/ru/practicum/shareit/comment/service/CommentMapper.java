package ru.practicum.shareit.comment.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.comment.model.Comment;

@Component
public class CommentMapper {
    public static CommentDtoExp toCommentDtoExp(Comment comment) {
        return CommentDtoExp.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
