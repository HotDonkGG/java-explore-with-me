package ru.yandex.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.comments.CommentFullDto;
import ru.yandex.practicum.dto.comments.CommentNewDto;
import ru.yandex.practicum.dto.comments.CommentShortDto;
import ru.yandex.practicum.service.comments.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    /**
     * Добавляет новый комментарий к событию.
     *
     * @param commentNewDto Объект с информацией о новом комментарии.
     * @param userId        Идентификатор пользователя.
     * @param eventId       Идентификатор события.
     * @return Объект CommentFullDto, представляющий добавленный комментарий.
     */
    @PostMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentFullDto addComment(@Valid @RequestBody CommentNewDto commentNewDto,
                                     @PathVariable Long userId,
                                     @PathVariable Long eventId) {
        return commentService.addComment(userId, eventId, commentNewDto);
    }

    /**
     * Обновляет существующий комментарий.
     *
     * @param commentNewDto Объект с информацией о новом комментарии.
     * @param userId        Идентификатор пользователя.
     * @param commentId     Идентификатор комментария.
     * @return Объект CommentFullDto, представляющий обновлённый комментарий.
     */
    @PatchMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentFullDto updateComment(@Valid @RequestBody CommentNewDto commentNewDto,
                                        @PathVariable Long userId,
                                        @PathVariable Long commentId) {
        return commentService.updateComment(userId, commentId, commentNewDto);
    }

    /**
     * Удаляет комментарий пользователя по его идентификатору.
     *
     * @param userId    Идентификатор пользователя.
     * @param commentId Идентификатор удаляемого комментария.
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deletePrivateComment(userId, commentId);
    }

    /**
     * Получает список коротких описаний комментариев пользователя на основе указанных параметров.
     *
     * @param userId     Идентификатор пользователя.
     * @param rangeStart Необязательный параметр, указывающий начальную дату для получения комментариев.
     * @param rangeEnd   Необязательный параметр, указывающий конечную дату для получения комментариев.
     * @param from       Начальный индекс результирующего набора (по умолчанию 0).
     * @param size       Количество комментариев для получения (по умолчанию 10).
     * @return Список объектов CommentShortDto, представляющих полученные короткие описания комментариев.
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentShortDto> getCommentsByUserId(@PathVariable Long userId,
                                                     @RequestParam(required = false, name = "rangeStart") String rangeStart,
                                                     @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
                                                     @PositiveOrZero @RequestParam(name = "from",
                                                             defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size",
                                                             defaultValue = "10") Integer size) {
        return commentService.getCommentsByUserId(rangeStart, rangeEnd, userId, from, size);
    }
}