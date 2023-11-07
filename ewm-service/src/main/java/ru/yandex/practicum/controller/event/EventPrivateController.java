package ru.yandex.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventNewDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.dto.event.EventUpdateDto;
import ru.yandex.practicum.dto.request.RequestDto;
import ru.yandex.practicum.dto.request.RequestUpdateDtoRequest;
import ru.yandex.practicum.dto.request.RequestUpdateDtoResult;
import ru.yandex.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;

    /**
     * Создает новое событие для пользователя.
     *
     * @param eventNewDto Объект, содержащий информацию о новом событии.
     * @param userId      Идентификатор пользователя, для которого создается событие.
     * @return Созданное событие.
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto addEvent(@Valid @RequestBody EventNewDto eventNewDto,
                                 @PathVariable Long userId) {
        log.info("User id {}, add Event {} ", userId, eventNewDto.getAnnotation());
        return eventService.addEvent(userId, eventNewDto);
    }

    /**
     * Получает список событий пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Стартовая позиция в результате.
     * @param size   Количество элементов на странице.
     * @return Список событий пользователя.
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventShortDto> getAllEventsByUserId(@PathVariable Long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("List events for User Id {}. Where from = {}, size = {}", userId, from, size);
        return eventService.getAllEventsByUserId(userId, from, size);
    }

    /**
     * Получение события по его идентификатору для указанного пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Объект EventFullDto, представляющий событие.
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("Get Event id {}, for User id {} ", eventId, userId);
        return eventService.getUserEventById(userId, eventId);
    }

    /**
     * Обновление события для указанного пользователя по его идентификатору.
     *
     * @param eventUpdateDto Объект EventUpdateDto, содержащий данные для обновления события.
     * @param userId         Идентификатор пользователя.
     * @param eventId        Идентификатор события.
     * @return Обновленный объект EventFullDto, представляющий событие.
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto updateEventByUserId(@RequestBody @Valid EventUpdateDto eventUpdateDto,
                                            @PathVariable Long userId,
                                            @PathVariable Long eventId) {

        log.info("User id {}, update Event {} ", eventId, eventUpdateDto.getAnnotation());
        return eventService.updateEventByUserId(eventUpdateDto, userId, eventId);
    }

    /**
     * Получение списка заявок для события по его идентификатору и указанного пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Список объектов RequestDto, представляющих заявки для события.
     */
    @GetMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    private List<RequestDto> getRequestsForEventIdByUserId(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Get all requests for event id{} by user Id{}.", eventId, userId);
        return eventService.getRequestsForEventIdByUserId(userId, eventId);
    }

    /**
     * Обновление статуса заявок для события по его идентификатору и указанного пользователя.
     *
     * @param userId     Идентификатор пользователя.
     * @param eventId    Идентификатор события.
     * @param requestDto Объект RequestUpdateDtoRequest, содержащий данные для обновления статуса заявок.
     * @return Объект RequestUpdateDtoResult, представляющий результат обновления статуса заявок.
     */
    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    private RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(@PathVariable Long userId,
                                                                          @PathVariable Long eventId,
                                                                          @RequestBody RequestUpdateDtoRequest requestDto) {
        log.info("Update status request for event id{}, by user id{}.", eventId, userId);
        return eventService.updateStatusRequestsForEventIdByUserId(requestDto, userId, eventId);
    }
}