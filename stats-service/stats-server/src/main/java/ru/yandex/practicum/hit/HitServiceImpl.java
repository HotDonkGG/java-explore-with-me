package ru.yandex.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.StatsDto;
import ru.yandex.practicum.exceptions.StatisticsValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private HitRepository hitRepository;

    /**
     * Добавляет новый хит.
     *
     * @param hitDto объект с данными о хите
     */
    @Override
    @Transactional
    public void addHit(HitDto hitDto) {
        hitRepository.save(HitMapper.returnHit(hitDto));
    }

    /**
     * Находит статистику по хитам за указанный период времени.
     *
     * @param start  начало периода времени
     * @param end    конец периода времени
     * @param uris   список URL-адресов, по которым велась статистика
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return список объектов со статистикой по хитам
     */
    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new StatisticsValidationException("Start must be after End");
            }
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                log.info("Get all stats by uniq ip");
                return hitRepository.findAllStatsByUniqueIp(start, end);
            } else {
                log.info("Get all stats");
                return hitRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                log.info("Get all stats by uri and uniq ip");
                return hitRepository.findStatsByUrisByUniqueIp(start, end, uris);
            } else {
                log.info("Get all stats by uri");
                return hitRepository.findStatsByUris(start, end, uris);
            }
        }
    }
}
