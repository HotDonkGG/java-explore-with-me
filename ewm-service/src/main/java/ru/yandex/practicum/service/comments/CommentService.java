package ru.yandex.practicum.service.comments;

import ru.yandex.practicum.dto.comments.CommentFullDto;
import ru.yandex.practicum.dto.comments.CommentNewDto;
import ru.yandex.practicum.dto.comments.CommentShortDto;

import java.util.List;

public interface CommentService {

    /**
     * Добавляет новый комментарий для события.
     *
     * @param userId        Идентификатор пользователя, оставляющего комментарий.
     * @param eventId       Идентификатор события, к которому добавляется комментарий.
     * @param commentNewDto Объект CommentNewDto с данными для создания комментария.
     * @return Объект CommentFullDto, представляющий добавленный комментарий.
     */
    CommentFullDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto);

    /**
     * Обновляет существующий комментарий пользователя.
     *
     * @param userId        Идентификатор пользователя, обновляющего комментарий.
     * @param commentId     Идентификатор комментария, который необходимо обновить.
     * @param commentNewDto Объект CommentNewDto с данными для обновления комментария.
     * @return Объект CommentFullDto, представляющий обновлённый комментарий.
     */
    CommentFullDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto);

    /**
     * Удаляет приватный комментарий пользователя.
     *
     * @param userId    Идентификатор пользователя, удаляющего комментарий.
     * @param commentId Идентификатор комментария, который необходимо удалить.
     */
    void deletePrivateComment(Long userId, Long commentId);

    /**
     * Получает список кратких описаний комментариев пользователя с учетом дополнительных параметров.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param userId     Идентификатор пользователя, комментарии которого необходимо получить.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentShortDto, представляющих краткие описания комментариев пользователя.
     */
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

    /**
     * Удаляет комментарий с административными правами.
     *
     * @param commentId Идентификатор комментария, который необходимо удалить.
     */
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