package ru.yandex.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.HitDto;
import ru.yandex.practicum.StatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.Util.FORMATTER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HitController {

    private final HitService hitService;

    /**
     * Добавляет новый хит.
     *
     * @param hitDto объект с данными о хите
     */
    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody HitDto hitDto) {
        log.info("Hit created");
        hitService.addHit(hitDto);
    }

    /**
     * Находит статистику по хитам за указанный период времени.
     *
     * @param start  начало периода времени
     * @param end    конец периода времени
     * @param uris   список URL-адресов, по которым велась статистика (необязательный параметр)
     * @param unique флаг, указывающий, считать ли уникальные хиты (необязательный параметр,
     *               значение по умолчанию - false)
     * @return список объектов со статистикой по хитам
     */
    @GetMapping("/stats")
    @ResponseStatus(value = HttpStatus.OK)
    public List<StatsDto> getStats(@RequestParam("start") String start,
                                   @RequestParam("end") String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, FORMATTER);
        log.info("Get stats");
        return hitService.getStats(startTime, endTime, uris, unique);
    }
}