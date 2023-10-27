package ru.yandex.practicum;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    /**
     * Добавляет новый хит.
     *
     * @param hitDto объект с данными о хите
     */
    void addHit(HitDto hitDto);

    /**
     * Находит статистику по хитам за указанный период времени.
     *
     * @param start начало периода времени
     * @param end конец периода времени
     * @param uris список URL-адресов, по которым велась статистика
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return список объектов со статистикой по хитам
     */
    List<StatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
