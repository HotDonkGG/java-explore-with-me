package ru.yandex.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.comments.CommentShortDto;
import ru.yandex.practicum.service.comments.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {

    private final CommentService commentService;

    /**
     * Получает список кратких описаний комментариев по идентификатору события.
     *
     * @param eventId    Идентификатор события.
     * @param rangeStart Необязательный параметр, указывающий начальную дату для получения комментариев.
     * @param rangeEnd   Необязательный параметр, указывающий конечную дату для получения комментариев.
     * @param from       Начальный индекс результирующего набора (по умолчанию 0).
     * @param size       Количество комментариев для получения (по умолчанию 10).
     * @return Список объектов CommentShortDto, представляющих полученные короткие описания комментариев.
     */
    @GetMapping("{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentShortDto> getCommentsByEventId(@PathVariable Long eventId,
                                                      @RequestParam(required = false, name = "rangeStart") String rangeStart,
                                                      @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
                                                      @PositiveOrZero @RequestParam(name = "from",
                                                              defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size",
                                                              defaultValue = "10") Integer size) {
        return commentService.getCommentsByEventId(rangeStart, rangeEnd, eventId, from, size);
    }
}