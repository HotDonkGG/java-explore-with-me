package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

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
     * @param start начало периода времени
     * @param end конец периода времени
     * @param uris список URL-адресов, по которым велась статистика
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return список объектов со статистикой по хитам
     */
    @Override
    public List<StatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            log.info("");
            if (unique) {
                log.info("ff");
                return hitRepository.findAllStatsByUniqueIp(start, end);
            } else {
                return hitRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                log.info("ff");
                return hitRepository.findStatsByUrisByUniqueIp(start, end, uris);
            } else {
                log.info("ff");
                return hitRepository.findStatsByUris(start, end, uris);
            }
        }

    }
}
