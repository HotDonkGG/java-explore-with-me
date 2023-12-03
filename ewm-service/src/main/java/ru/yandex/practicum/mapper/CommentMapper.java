package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.comments.CommentFullDto;
import ru.yandex.practicum.dto.comments.CommentNewDto;
import ru.yandex.practicum.dto.comments.CommentShortDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.Util.CURRENT_TIME;

@UtilityClass
public class CommentMapper {

    /**
     * Преобразует CommentNewDto, User и Event в объект Comment.
     *
     * @param commentNewDto Объект CommentNewDto с данными для создания нового комментария.
     * @param user          Объект User, представляющий пользователя, оставившего комментарий.
     * @param event         Объект Event, представляющий событие, к которому оставлен комментарий.
     * @return Объект Comment, представляющий созданный комментарий.
     */
    public static Comment returnComment(CommentNewDto commentNewDto, User user, Event event) {
        Comment comment = Comment.builder()
                .user(user)
                .event(event)
                .message(commentNewDto.getMessage())
                .created(CURRENT_TIME)
                .build();
        return comment;
    }

    /**
     * Преобразует Comment в объект CommentFullDto.
     *
     * @param comment Объект Comment, который необходимо преобразовать.
     * @return Объект CommentFullDto, представляющий полный формат комментария.
     */
    public static CommentFullDto returnCommentFullDto(Comment comment) {
        CommentFullDto commentFullDto = CommentFullDto.builder()
                .id(comment.getId())
                .user(UserMapper.returnUserDto(comment.getUser()))
                .event(EventMapper.returnEventFullDto(comment.getEvent()))
                .message(comment.getMessage())
                .created(comment.getCreated())
                .build();
        return commentFullDto;
    }

    /**
     * Преобразует Comment в объект CommentShortDto.
     *
     * @param comment Объект Comment, который необходимо преобразовать.
     * @return Объект CommentShortDto, представляющий краткое описание комментария.
     */
    public static CommentShortDto returnCommentShortDto(Comment comment) {
        CommentShortDto commentShortDto = CommentShortDto.builder()
                .userName(comment.getUser().getName())
                .eventTitle(comment.getEvent().getTitle())
                .message(comment.getMessage())
                .created(comment.getCreated())
                .build();
        return commentShortDto;
    }

    /**
     * Преобразует Iterable<Comment> в список объектов CommentFullDto.
     *
     * @param comments Iterable<Comment>, которое необходимо преобразовать.
     * @return Список объектов CommentFullDto, представляющих полные форматы комментариев.
     */
    public static List<CommentFullDto> returnCommentFullDtoList(Iterable<Comment> comments) {
        List<CommentFullDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(returnCommentFullDto(comment));
        }
        return result;
    }

    /**
     * Преобразует Iterable<Comment> в список объектов CommentShortDto.
     *
     * @param comments Iterable<Comment>, которое необходимо преобразовать.
     * @return Список объектов CommentShortDto, представляющих краткие описания комментариев.
     */
    public static List<CommentShortDto> returnCommentShortDtoList(Iterable<Comment> comments) {
        List<CommentShortDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(returnCommentShortDto(comment));
        }
        return result;
    }
}