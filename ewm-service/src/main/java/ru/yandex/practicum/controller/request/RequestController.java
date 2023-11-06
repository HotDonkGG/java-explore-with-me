package ru.yandex.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.request.RequestService;
import ru.yandex.practicum.dto.request.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private RequestService requestService;

    /**
     * Создает новую заявку для указанного пользователя на указанное событие.
     *
     * @param userId  Идентификатор пользователя, создающего заявку.
     * @param eventId Идентификатор события, на которое создается заявка.
     * @return Объект RequestDto, представляющий созданную заявку.
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        log.info("User id {} added request for Event id {}.", userId, eventId);
        return requestService.addRequest(userId, eventId);
    }

    /**
     * Возвращает список заявок, созданных указанным пользователем.
     *
     * @param userId Идентификатор пользователя.
     * @return Список RequestDto, представляющий заявки пользователя.
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<RequestDto> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Get all requests by user id{}.", userId);
        return requestService.getRequestsByUserId(userId);
    }

    /**
     * Отменяет заявку пользователя по её идентификатору.
     *
     * @param userId    Идентификатор пользователя, отменяющего заявку.
     * @param requestId Идентификатор заявки, которую необходимо отменить.
     * @return Объект RequestDto, представляющий отмененную заявку.
     */
    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("User id{} canceled request id{}.", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}