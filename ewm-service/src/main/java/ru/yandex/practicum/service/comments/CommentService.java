package ru.yandex.practicum.service.comments;

import ru.yandex.practicum.dto.comments.CommentFullDto;
import ru.yandex.practicum.dto.comments.CommentNewDto;
import ru.yandex.practicum.dto.comments.CommentShortDto;

import java.util.List;

public interface CommentService {

    CommentFullDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto);

    CommentFullDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto);

    void deletePrivateComment(Long userId, Long commentId);

    List<CommentShortDto> getCommentsByUserId(String rangeStart, String rangeEnd, Long userId, Integer from, Integer size);

    /**
     * Получает список полных описаний комментариев с учетом дополнительных параметров.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentFullDto, представляющих полные описания комментариев.
     */
    List<CommentFullDto> getComments(String rangeStart, String rangeEnd, Integer from, Integer size);

    void deleteAdminComment(Long commentId);

    /**
     * Получает список кратких описаний комментариев по идентификатору события с учетом дополнительных параметров.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param eventId    Идентификатор события, комментарии к которому необходимо получить.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentShortDto, представляющих краткие описания комментариев по идентификатору события.
     */
    List<CommentShortDto> getCommentsByEventId(String rangeStart, String rangeEnd, Long eventId, Integer from, Integer size);
}