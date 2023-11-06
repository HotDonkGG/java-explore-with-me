package ru.yandex.practicum.repository;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Возвращает список заявок, созданных указанным пользователем.
     *
     * @param userId Идентификатор пользователя.
     * @return Список объектов Request, представляющий заявки пользователя.
     */
    List<Request> findByRequesterId(Long userId);

    /**
     * Возвращает список заявок для указанного события.
     *
     * @param eventId Идентификатор события.
     * @return Список объектов Request, представляющий заявки для события.
     */
    List<Request> findByEventId(Long eventId);

    /**
     * Возвращает заявку, созданную указанным пользователем для указанного события (если существует).
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Объект Optional, содержащий заявку, если она существует, в противном случае пустой Optional.
     */
    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    /**
     * Подсчитывает количество заявок для указанного события с указанным статусом.
     *
     * @param eventId Идентификатор события.
     * @param status  Статус заявки.
     * @return Количество заявок с указанным статусом для события.
     */
    Long countAllByEventIdAndStatus(Long eventId, Status status);
}
