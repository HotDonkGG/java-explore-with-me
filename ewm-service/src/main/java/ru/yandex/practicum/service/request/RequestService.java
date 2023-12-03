package ru.yandex.practicum.service.request;

import ru.yandex.practicum.dto.request.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto addRequest(Long userId, Long eventId);

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