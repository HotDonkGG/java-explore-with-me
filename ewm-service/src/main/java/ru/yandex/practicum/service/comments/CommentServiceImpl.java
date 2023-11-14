package ru.yandex.practicum.service.comments;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.comments.CommentFullDto;
import ru.yandex.practicum.dto.comments.CommentNewDto;
import ru.yandex.practicum.dto.comments.CommentShortDto;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.util.UnionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.Util.CURRENT_TIME;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UnionService unionService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentFullDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        Comment comment = CommentMapper.returnComment(commentNewDto, user, event);
        comment = commentRepository.save(comment);
        return CommentMapper.returnCommentFullDto(comment);
    }

    /**
     * Обновляет существующий комментарий пользователя.
     *
     * @param userId        Идентификатор пользователя, оставившего комментарий.
     * @param commentId     Идентификатор обновляемого комментария.
     * @param commentNewDto DTO нового содержания комментария.
     * @return DTO полной информации об обновленном комментарии.
     */
    @Override
    @Transactional
    public CommentFullDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto) {
        Comment comment = unionService.getCommentOrNotFound(commentId);
        if (!userId.equals(comment.getUser().getId())) {
            throw new ConflictException(String.format("User %s is not the author of the comment %s.", userId, commentId));
        }
        if (commentNewDto.getMessage() != null && !commentNewDto.getMessage().isBlank()) {
            comment.setMessage(commentNewDto.getMessage());
        }
        comment = commentRepository.save(comment);
        return CommentMapper.returnCommentFullDto(comment);
    }

    /**
     * Удаляет комментарий пользователя.
     *
     * @param userId    Идентификатор пользователя, удаляющего комментарий.
     * @param commentId Идентификатор удаляемого комментария.
     */
    @Override
    @Transactional
    public void deletePrivateComment(Long userId, Long commentId) {
        Comment comment = unionService.getCommentOrNotFound(commentId);
        unionService.getUserOrNotFound(userId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new ConflictException(String.format("User %s is not the author of the comment %s.", userId, commentId));
        }
        commentRepository.deleteById(commentId);
    }

    /**
     * Получает список кратких описаний комментариев пользователя.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param userId     Идентификатор пользователя, комментарии которого необходимо получить.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentShortDto, представляющих краткие описания комментариев пользователя.
     */
    @Override
    public List<CommentShortDto> getCommentsByUserId(String rangeStart, String rangeEnd,
                                                     Long userId, Integer from, Integer size) {
        unionService.getUserOrNotFound(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime startTime = Objects.requireNonNull(unionService.parseDate(rangeStart));
        LocalDateTime endTime = Objects.requireNonNull(unionService.parseDate(rangeEnd));
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("Start must be after End");
        }
        if (endTime.isAfter(CURRENT_TIME) || startTime.isAfter(CURRENT_TIME)) {
            throw new ValidationException("date must be the past");
        }
        List<Comment> commentList = commentRepository.getCommentsByUserId(userId, startTime, endTime, pageRequest);
        return CommentMapper.returnCommentShortDtoList(commentList);
    }

    /**
     * Получает список полных описаний комментариев с учетом дополнительных параметров.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentFullDto, представляющих полные описания комментариев.
     */
    @Override
    public List<CommentFullDto> getComments(String rangeStart, String rangeEnd, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime startTime = Objects.requireNonNull(unionService.parseDate(rangeStart));
        LocalDateTime endTime = Objects.requireNonNull(unionService.parseDate(rangeEnd));
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("Start must be after End");
        }
        if (endTime.isAfter(CURRENT_TIME) || startTime.isAfter(CURRENT_TIME)) {
            throw new ValidationException("date must be the past");
        }
        List<Comment> commentList = commentRepository.getComments(startTime, endTime, pageRequest);
        return CommentMapper.returnCommentFullDtoList(commentList);
    }

    @Override
    @Transactional
    public void deleteAdminComment(Long commentId) {
        unionService.getCommentOrNotFound(commentId);
        commentRepository.deleteById(commentId);
    }

    /**
     * Получает список кратких описаний комментариев для указанного события.
     *
     * @param rangeStart Начальная дата для фильтрации комментариев.
     * @param rangeEnd   Конечная дата для фильтрации комментариев.
     * @param eventId    Идентификатор события, комментарии к которому необходимо получить.
     * @param from       Начальный индекс результирующего набора.
     * @param size       Количество комментариев для получения.
     * @return Список объектов CommentShortDto, представляющих краткие описания комментариев к событию.
     */
    @Override
    public List<CommentShortDto> getCommentsByEventId(String rangeStart, String rangeEnd, Long eventId,
                                                      Integer from, Integer size) {
        unionService.getEventOrNotFound(eventId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime startTime = Objects.requireNonNull(unionService.parseDate(rangeStart));
        LocalDateTime endTime = Objects.requireNonNull(unionService.parseDate(rangeEnd));
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("Start must be after End");
        }
        if (endTime.isAfter(CURRENT_TIME) || startTime.isAfter(CURRENT_TIME)) {
            throw new ValidationException("date must be the past");
        }
        List<Comment> commentList = commentRepository.getCommentsByEventId(eventId, startTime, endTime, pageRequest);
        return CommentMapper.returnCommentShortDtoList(commentList);
    }
}