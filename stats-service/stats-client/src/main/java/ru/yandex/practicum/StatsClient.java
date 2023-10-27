package ru.yandex.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    /**
     * Добавляет новый хит в статистику.
     *
     * @param hitDto объект с данными о хите
     * @return ответ от сервера статистики
     */
    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    /**
     * Находит статистику за указанный период времени.
     *
     * @param start начало периода времени
     * @param end конец периода времени
     * @param uris массив URL-адресов, по которым велась статистика
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return ответ от сервера статистики
     */
    public ResponseEntity<Object> findStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    /**
     * Находит статистику за указанный период времени без фильтрации по URL-адресам.
     *
     * @param start начало периода времени
     * @param end конец периода времени
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return ответ от сервера статистики
     */
    public ResponseEntity<Object> findStatsWithoutUris(String start, String end, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique);
        return get("/stats?start={start}&end={end}&unique={unique}", parameters);
    }
}