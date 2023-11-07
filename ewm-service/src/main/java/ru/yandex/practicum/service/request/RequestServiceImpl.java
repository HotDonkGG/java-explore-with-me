package ru.yandex.practicum.service.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.enums.State;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.dto.request.RequestDto;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.mapper.RequestMapper;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.Request;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.RequestRepository;
import ru.yandex.practicum.service.util.UnionService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UnionService unionService;

    /**
     * Создает новую заявку для указанного пользователя на указанное событие.
     *
     * @param userId  Идентификатор пользователя, создающего заявку.
     * @param eventId Идентификатор события, на которое создается заявка.
     * @return Объект RequestDto, представляющий созданную заявку.
     * @throws ConflictException если создание заявки не удалось из-за конфликтов.
     */
    @Override
    @Transactional
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        if (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictException(String.format("Event %s requests exceed the limit", event));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("Initiator, user id %s cannot give a request to participate in his event",
                    user.getId()));
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(String.format("You have already applied to participate in Event %s",
                    event.getTitle()));
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException(String.format("Event %s has not been published, you cannot request participation",
                    eventId));
        } else {
            Request request = Request.builder()
                    .requester(user)
                    .event(event)
                    .created(LocalDateTime.now())
                    .status(Status.PENDING)
                    .build();
            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                request.setStatus(Status.CONFIRMED);
                request = requestRepository.save(request);
                event.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED));
                eventRepository.save(event);

                return RequestMapper.returnRequestDto(request);
            }
            request = requestRepository.save(request);
            return RequestMapper.returnRequestDto(request);
        }
    }

    /**
     * Возвращает список заявок, созданных указанным пользователем.
     *
     * @param userId Идентификатор пользователя.
     * @return Список RequestDto, представляющий заявки пользователя.
     */
    @Override
    public List<RequestDto> getRequestsByUserId(Long userId) {
        unionService.getUserOrNotFound(userId);
        List<Request> requestList = requestRepository.findByRequesterId(userId);
        return RequestMapper.returnRequestDtoList(requestList);
    }

    /**
     * Отменяет заявку пользователя по её идентификатору.
     *
     * @param userId    Идентификатор пользователя, отменяющего заявку.
     * @param requestId Идентификатор заявки, которую необходимо отменить.
     * @return Объект RequestDto, представляющий отмененную заявку.
     */
    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        unionService.getUserOrNotFound(userId);
        Request request = unionService.getRequestOrNotFound(requestId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.returnRequestDto(requestRepository.save(request));
    }
}