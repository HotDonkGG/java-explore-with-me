package ru.yandex.practicum.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.StatsClient;
import ru.yandex.practicum.StatsDto;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventNewDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.dto.event.EventUpdateDto;
import ru.yandex.practicum.dto.request.RequestDto;
import ru.yandex.practicum.dto.request.RequestUpdateDtoRequest;
import ru.yandex.practicum.dto.request.RequestUpdateDtoResult;
import ru.yandex.practicum.enums.State;
import ru.yandex.practicum.enums.StateAction;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.mapper.EventMapper;
import ru.yandex.practicum.mapper.LocationMapper;
import ru.yandex.practicum.mapper.RequestMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.LocationRepository;
import ru.yandex.practicum.repository.RequestRepository;
import ru.yandex.practicum.service.util.UnionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.yandex.practicum.Util.START_HISTORY;
import static ru.yandex.practicum.enums.State.PUBLISHED;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final UnionService unionService;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatsClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Добавляет новое событие в систему.
     *
     * @param userId      Идентификатор пользователя, добавляющего событие.
     * @param eventNewDto DTO, представляющий новое добавляемое событие.
     * @return Полный DTO добавленного события.
     */
    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, EventNewDto eventNewDto) {
        User user = unionService.getUserOrNotFound(userId);
        Category category = unionService.getCategoryOrNotFound(eventNewDto.getCategory());
        Location location = locationRepository.save(LocationMapper.returnLocation(eventNewDto.getLocation()));
        Event event = EventMapper.returnEvent(eventNewDto, category, location, user);
        eventRepository.save(event);
        return EventMapper.returnEventFullDto(event);
    }

    /**
     * Получает список событий, инициированных пользователем.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Начальная позиция в списке.
     * @param size   Размер списка событий.
     * @return Список кратких DTO событий, инициированных пользователем.
     */
    @Override
    public List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        unionService.getUserOrNotFound(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageRequest);
        return EventMapper.returnEventShortDtoList(events);
    }

    /**
     * Получает полное описание события пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Полное описание события в виде DTO.
     */
    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        unionService.getUserOrNotFound(userId);
        unionService.getEventOrNotFound(eventId);
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        return EventMapper.returnEventFullDto(event);
    }

    /**
     * Обновляет событие пользователя по его идентификатору.
     *
     * @param eventUpdateDto DTO с обновленными данными события.
     * @param userId         Идентификатор пользователя.
     * @param eventId        Идентификатор события.
     * @return Полное описание обновленного события в виде DTO.
     */
    @Override
    @Transactional
    public EventFullDto updateEventByUserId(EventUpdateDto eventUpdateDto, Long userId, Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User %s is not the initiator of the event %s.", userId, eventId));
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException(String.format("User %s cannot update event %s that has already been published.", userId, eventId));
        }
        Event updateEvent = baseUpdateEvent(event, eventUpdateDto);
        return EventMapper.returnEventFullDto(updateEvent);
    }

    /**
     * Получает список запросов для события пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Список DTO запросов для события.
     */
    @Override
    public List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User %s is not the initiator of the event %s.",
                    userId,
                    eventId));
        }
        List<Request> requests = requestRepository.findByEventId(eventId);
        return RequestMapper.returnRequestDtoList(requests);
    }

    /**
     * Обновляет статус запросов для события пользователя по его идентификатору.
     *
     * @param requestDto DTO с информацией о запросах и их статусе.
     * @param userId     Идентификатор пользователя.
     * @param eventId    Идентификатор события.
     * @return Результат обновления статуса запросов.
     */
    @Override
    @Transactional
    public RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestDto,
                                                                         Long userId,
                                                                         Long eventId) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);
        RequestUpdateDtoResult result = RequestUpdateDtoResult.builder()
                .confirmedRequests(Collections.emptyList())
                .rejectedRequests(Collections.emptyList())
                .build();
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User %s is not the initiator of the event %s.", userId, eventId));
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Exceeded the limit of participants");
        }
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        long vacantPlace = event.getParticipantLimit() - event.getConfirmedRequests();
        List<Request> requestsList = requestRepository.findAllById(requestDto.getRequestIds());
        for (Request request : requestsList) {
            if (!request.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("Request must have status PENDING");
            }
            if (requestDto.getStatus().equals(Status.CONFIRMED) && vacantPlace > 0) {
                request.setStatus(Status.CONFIRMED);
                event.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED));
                confirmedRequests.add(request);
                vacantPlace--;
            } else {
                request.setStatus(Status.REJECTED);
                rejectedRequests.add(request);
            }
        }
        result.setConfirmedRequests(RequestMapper.returnRequestDtoList(confirmedRequests));
        result.setRejectedRequests(RequestMapper.returnRequestDtoList(rejectedRequests));
        eventRepository.save(event);
        requestRepository.saveAll(requestsList);
        return result;
    }

    /**
     * Обновляет событие администратором.
     *
     * @param eventUpdateDto DTO с обновленными данными события.
     * @param eventId        Идентификатор события.
     * @return Полное описание обновленного события в виде DTO.
     */
    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(EventUpdateDto eventUpdateDto, Long eventId) {
        Event event = unionService.getEventOrNotFound(eventId);
        if (eventUpdateDto.getStateAction() != null) {
            if (eventUpdateDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException(String.format("Event - %s, has already been published, cannot be published again ", event.getTitle()));
                }
                event.setPublishedOn(LocalDateTime.now());
                event.setState(PUBLISHED);
            } else {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException(String.format("Event - %s, cannot be canceled because its statute is not \"PENDING\"", event.getTitle()));
                }
                event.setState(State.CANCELED);
            }
        }
        Event updateEvent = baseUpdateEvent(event, eventUpdateDto);
        return EventMapper.returnEventFullDto(updateEvent);
    }

    /**
     * Получает список событий администратором, удовлетворяющих заданным параметрам.
     *
     * @param users      Список идентификаторов пользователей.
     * @param states     Список статусов событий.
     * @param categories Список идентификаторов категорий.
     * @param rangeStart Начальная дата и времени для фильтрации.
     * @param rangeEnd   Конечная дата и времени для фильтрации.
     * @param from       Начальная позиция в списке.
     * @param size       Размер списка событий.
     * @return Список полных DTO событий, удовлетворяющих параметрам.
     */
    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<String> states,
                                               List<Long> categories,
                                               String rangeStart,
                                               String rangeEnd,
                                               Integer from,
                                               Integer size) {
        LocalDateTime startTime = unionService.parseDate(rangeStart);
        LocalDateTime endTime = unionService.parseDate(rangeEnd);
        List<State> statesValue = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                statesValue.add(State.getStateValue(state));
            }
        }
        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                throw new ValidationException("Start must be after End");
            }
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findEventsByAdminFromParam(users,
                statesValue,
                categories,
                startTime,
                endTime,
                pageRequest);
        return EventMapper.returnEventFullDtoList(events);
    }

    /**
     * Получает полное описание события по его идентификатору для общего доступа.
     *
     * @param eventId Идентификатор события.
     * @param uri     URI запроса.
     * @param ip      IP-адрес клиента.
     * @return Полное описание события в виде DTO.
     */
    @Override
    public EventFullDto getEventById(Long eventId, String uri, String ip) {
        Event event = unionService.getEventOrNotFound(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException(Event.class, String.format("Event %s not published", eventId));
        }
        sendInfo(uri, ip);
        event.setViews(getViewsEventById(event.getId()));
        eventRepository.save(event);
        return EventMapper.returnEventFullDto(event);
    }

    /**
     * Получает список кратких описаний событий для общего доступа.
     *
     * @param text          Текст для поиска в описании события.
     * @param categories    Список идентификаторов категорий.
     * @param paid          Флаг, указывающий на платные события.
     * @param rangeStart    Начальная дата и времени для фильтрации.
     * @param rangeEnd      Конечная дата и времени для фильтрации.
     * @param onlyAvailable Флаг, указывающий на доступные события.
     * @param sort          Параметр сортировки событий.
     * @param from          Начальная позиция в списке.
     * @param size          Размер списка событий.
     * @param uri           URI запроса.
     * @param ip            IP-адрес клиента.
     * @return Список кратких DTO событий для общего доступа.
     */
    @Override
    public List<EventShortDto> getEventsByPublic(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 Integer from,
                                                 Integer size,
                                                 String uri,
                                                 String ip) {
        LocalDateTime startTime = unionService.parseDate(rangeStart);
        LocalDateTime endTime = unionService.parseDate(rangeEnd);
        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                throw new ValidationException("Start must be after End");
            }
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findEventsByPublicFromParam(text,
                categories,
                paid,
                startTime,
                endTime,
                onlyAvailable,
                sort,
                pageRequest);
        sendInfo(uri, ip);
        for (Event event : events) {
            event.setViews(getViewsEventById(event.getId()));
            eventRepository.save(event);
        }
        return EventMapper.returnEventShortDtoList(events);
    }

    /**
     * Обновляет событие согласно данным из DTO.
     *
     * @param event          Событие, которое нужно обновить.
     * @param eventUpdateDto DTO с обновленными данными события.
     * @return Обновленное событие.
     */
    private Event baseUpdateEvent(Event event, EventUpdateDto eventUpdateDto) {
        if (eventUpdateDto.getAnnotation() != null && !eventUpdateDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getCategory() != null) {
            event.setCategory(unionService.getCategoryOrNotFound(eventUpdateDto.getCategory()));
        }
        if (eventUpdateDto.getDescription() != null && !eventUpdateDto.getDescription().isBlank()) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }
        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(LocationMapper.returnLocation(eventUpdateDto.getLocation()));
        }
        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }
        if (eventUpdateDto.getStateAction() != null) {
            if (eventUpdateDto.getStateAction() == StateAction.PUBLISH_EVENT) {
                event.setState(PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventUpdateDto.getStateAction() == StateAction.REJECT_EVENT ||
                    eventUpdateDto.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else if (eventUpdateDto.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            }
        }
        if (eventUpdateDto.getTitle() != null && !eventUpdateDto.getTitle().isBlank()) {
            event.setTitle(eventUpdateDto.getTitle());
        }
        locationRepository.save(event.getLocation());
        return eventRepository.save(event);
    }

    /**
     * Отправляет информацию о запросе к стороннему сервису.
     *
     * @param uri URI запроса.
     * @param ip  IP-адрес клиента.
     */
    private void sendInfo(String uri, String ip) {
        HitDto hitDto = HitDto.builder()
                .app("ewm-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        client.addHit(hitDto);
    }

    /**
     * Получает количество просмотров события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Количество просмотров события.
     */
    private Long getViewsEventById(Long eventId) {
        String uri = "/events/" + eventId;
        ResponseEntity<Object> response = client.findStats(START_HISTORY, LocalDateTime.now(), uri, true);
        List<StatsDto> result = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });
        if (result.isEmpty()) {
            return 0L;
        } else {
            return result.get(0).getHits();
        }
    }
}