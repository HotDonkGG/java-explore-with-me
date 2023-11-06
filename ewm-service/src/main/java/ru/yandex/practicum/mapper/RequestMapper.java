package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.dto.request.RequestDto;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestMapper {
    /**
     * Преобразует объект Request в объект RequestDto.
     *
     * @param request Объект Request, который необходимо преобразовать.
     * @return Объект RequestDto, представляющий преобразованные данные из Request.
     */
    public static RequestDto returnRequestDto(Request request) {
        RequestDto requestDto = RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
        return requestDto;
    }

    /**
     * Преобразует объект RequestDto в объект Request.
     *
     * @param requestDto Объект RequestDto, который необходимо преобразовать.
     * @param event      Объект Event, на который создается заявка.
     * @param user       Объект User, создающий заявку.
     * @return Объект Request, представляющий преобразованные данные из RequestDto.
     */
    public Request returnRequest(RequestDto requestDto, Event event, User user) {
        Request request = Request.builder()
                .id(requestDto.getId())
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(Status.PENDING)
                .build();
        return request;
    }

    /**
     * Преобразует список объектов Request в список объектов RequestDto.
     *
     * @param requests Список объектов Request, который необходимо преобразовать.
     * @return Список объектов RequestDto, представляющий преобразованные данные из Request.
     */
    public static List<RequestDto> returnRequestDtoList(Iterable<Request> requests) {
        List<RequestDto> result = new ArrayList<>();
        for (Request request : requests) {
            result.add(returnRequestDto(request));
        }
        return result;
    }
}