package ru.yandex.practicum.service.request;

import ru.yandex.practicum.dto.request.RequestDto;

import java.util.List;

public interface RequestService {
    /**
     * Создает новую заявку для указанного пользователя на указанное событие.
     *
     * @param userId  Идентификатор пользователя, создающего заявку.
     * @param eventId Идентификатор события, на которое создается заявка.
     * @return Объект RequestDto, представляющий созданную заявку.
     */
    RequestDto addRequest(Long userId, Long eventId);

    /**
     * Возвращает список заявок, созданных указанным пользователем.
     *
     * @param userId Идентификатор пользователя.
     * @return Список RequestDto, представляющий заявки пользователя.
     */
    List<RequestDto> getRequestsByUserId(Long userId);

    /**
     * Отменяет заявку пользователя по её идентификатору.
     *
     * @param userId    Идентификатор пользователя, отменяющего заявку.
     * @param requestId Идентификатор заявки, которую необходимо отменить.
     * @return Объект RequestDto, представляющий отмененную заявку.
     */
    RequestDto cancelRequest(Long userId, Long requestId);
}