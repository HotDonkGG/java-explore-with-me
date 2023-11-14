package ru.yandex.practicum.service.event;

import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventNewDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.dto.event.EventUpdateDto;
import ru.yandex.practicum.dto.request.RequestDto;
import ru.yandex.practicum.dto.request.RequestUpdateDtoRequest;
import ru.yandex.practicum.dto.request.RequestUpdateDtoResult;

import java.util.List;

public interface EventService {

    EventFullDto addEvent(Long userId, EventNewDto eventnewDto);

    /**
     * Возвращает список краткой информации о событиях, созданных указанным пользователем, с применением пагинации.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Начальная позиция списка событий.
     * @param size   Размер страницы.
     * @return Список EventShortDto, представляющий краткую информацию о событиях пользователя с примененной пагинацией.
     */
    List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size);

    /**
     * Возвращает полную информацию о событии пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Объект EventFullDto, представляющий полную информацию о событии пользователя.
     */
    EventFullDto getUserEventById(Long userId, Long eventId);

    /**
     * Обновляет информацию о событии пользователя по его идентификатору.
     *
     * @param eventUpdateDto Данные для обновления события.
     * @param userId         Идентификатор пользователя.
     * @param eventId        Идентификатор события.
     * @return Объект EventFullDto, представляющий обновленное событие пользователя.
     */
    EventFullDto updateEventByUserId(EventUpdateDto eventUpdateDto, Long userId, Long eventId);

    /**
     * Возвращает список запросов для события пользователя по их идентификатору.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Список RequestDto, представляющий запросы для указанного события пользователя.
     */
    List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId);

    /**
     * Обновляет статус запросов для события пользователя по их идентификатору.
     *
     * @param requestDto Данные для обновления статуса запросов.
     * @param userId     Идентификатор пользователя.
     * @param eventId    Идентификатор события.
     * @return Объект RequestUpdateDtoResult, представляющий результат обновления статуса запросов.
     */
    RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestDto,
                                                                  Long userId, Long eventId);

    /**
     * Обновляет информацию о событии администратором по его идентификатору.
     *
     * @param eventUpdateDto Данные для обновления события.
     * @param eventId        Идентификатор события.
     * @return Объект EventFullDto, представляющий обновленное событие.
     */
    EventFullDto updateEventByAdmin(EventUpdateDto eventUpdateDto, Long eventId);

    /**
     * Возвращает список событий администратора с применением фильтрации и пагинации.
     *
     * @param users      Список идентификаторов пользователей.
     * @param states     Список состояний событий.
     * @param categories Список идентификаторов категорий событий.
     * @param startTime  Начальная дата и времени.
     * @param endTime    Конечная дата и времени.
     * @param from       Начальная позиция списка событий.
     * @param size       Размер страницы.
     * @return Список EventFullDto, представляющий события администратора с примененной фильтрацией и пагинацией.
     */
    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        String startTime, String endTime, Integer from, Integer size);

    /**
     * Возвращает полную информацию о событии по его идентификатору с учетом URI и IP-адреса.
     *
     * @param eventId Идентификатор события.
     * @param uri     URI запроса.
     * @param ip      IP-адрес клиента.
     * @return Объект EventFullDto, представляющий полную информацию о событии с учетом URI и IP-адреса.
     */
    EventFullDto getEventById(Long eventId, String uri, String ip);

    /**
     * Возвращает список краткой информации о событиях с учетом фильтрации и пагинации для общедоступных событий.
     *
     * @param text          Текстовый запрос для поиска событий.
     * @param categories    Список идентификаторов категорий событий.
     * @param paid          Флаг, указывающий на платные события.
     * @param startTime     Начальная дата и времени.
     * @param endTime       Конечная дата и времени.
     * @param onlyAvailable Флаг, указывающий на доступность событий.
     * @param sort          Способ сортировки результатов.
     * @param from          Начальная позиция списка событий.
     * @param size          Размер страницы.
     * @param uri           URI запроса.
     * @param ip            IP-адрес клиента.
     * @return Список EventShortDto, представляющий краткую информацию о общедоступных событиях с
     * учетом фильтрации и пагинации.
     */
    List<EventShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                          String startTime, String endTime, Boolean onlyAvailable,
                                          String sort, Integer from, Integer size, String uri, String ip);
}