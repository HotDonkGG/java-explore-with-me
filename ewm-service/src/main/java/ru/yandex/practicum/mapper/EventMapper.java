package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventNewDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.model.Category;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.Location;
import ru.yandex.practicum.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.enums.State.PENDING;

@UtilityClass
public class EventMapper {

    /**
     * Преобразует объект EventNewDto, Category, Location и User в объект Event.
     *
     * @param eventNewDto Данные для создания нового события.
     * @param category    Категория, к которой относится событие.
     * @param location    Местоположение, где будет проводиться событие.
     * @param user        Пользователь-инициатор события.
     * @return Объект Event, созданный на основе предоставленных данных.
     */
    public Event returnEvent(EventNewDto eventNewDto, Category category, Location location, User user) {
        Event event = Event.builder()
                .annotation(eventNewDto.getAnnotation())
                .category(category)
                .description(eventNewDto.getDescription())
                .eventDate(eventNewDto.getEventDate())
                .initiator(user)
                .location(location)
                .paid(eventNewDto.getPaid())
                .participantLimit(eventNewDto.getParticipantLimit())
                .requestModeration(eventNewDto.getRequestModeration())
                .createdOn(LocalDateTime.now())
                .views(0L)
                .state(PENDING)
                .confirmedRequests(0L)
                .title(eventNewDto.getTitle())
                .build();
        return event;
    }

    /**
     * Преобразует объект Event в объект EventFullDto.
     *
     * @param event Объект Event, который необходимо преобразовать.
     * @return Объект EventFullDto, представляющий событие с полной информацией.
     */
    public EventFullDto returnEventFullDto(Event event) {
        EventFullDto eventFullDto = EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.returnCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.returnUserShortDto(event.getInitiator()))
                .location(LocationMapper.returnLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
        return eventFullDto;
    }

    /**
     * Преобразует объект Event в объект EventShortDto.
     *
     * @param event Объект Event, который необходимо преобразовать.
     * @return Объект EventShortDto, представляющий событие с краткой информацией.
     */
    public EventShortDto returnEventShortDto(Event event) {
        EventShortDto eventShortDto = EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.returnCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.returnUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
        return eventShortDto;
    }

    /**
     * Преобразует список Event в список EventFullDto.
     *
     * @param events Итерируемая коллекция объектов Event.
     * @return Список объектов EventFullDto, представляющих события с полной информацией.
     */
    public List<EventFullDto> returnEventFullDtoList(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(returnEventFullDto(event));
        }
        return result;
    }

    /**
     * Преобразует список Event в список EventShortDto.
     *
     * @param events Итерируемая коллекция объектов Event.
     * @return Список объектов EventShortDto, представляющих события с краткой информацией.
     */
    public List<EventShortDto> returnEventShortDtoList(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            result.add(returnEventShortDto(event));
        }
        return result;
    }
}