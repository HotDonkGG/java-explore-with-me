package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody HitDto hitDto) {
        log.info("");
        hitService.addHit(hitDto);
    }

    /**
     * Находит статистику по хитам за указанный период времени.
     *
     * @param start начало периода времени
     * @param end конец периода времени
     * @param uris список URL-адресов, по которым велась статистика (необязательный параметр)
     * @param unique флаг, указывающий, считать ли уникальные хиты (необязательный параметр, значение по умолчанию - false)
     * @return список объектов со статистикой по хитам
     */
    @GetMapping("/stats")
    public List<StatsDto> findStats(@RequestParam("start") String start,
                                    @RequestParam("end") String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        log.info("");
        return hitService.findStats(startTime, endTime, uris, unique);
    }
}
