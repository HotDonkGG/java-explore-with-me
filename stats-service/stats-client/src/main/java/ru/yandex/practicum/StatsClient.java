package ru.yandex.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static ru.yandex.practicum.Util.DATE_FORMAT;

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

    /**
     * Конструктор StatsClient используется для создания клиента, взаимодействующего
     * с сервером статистики. Он инициализирует клиент с заданным URL-адресом сервера
     * и настраивает RestTemplate для отправки HTTP-запросов к этому серверу.
     *
     * @param serverUrl URL-адрес сервера статистики, к которому будет выполняться
     *                  HTTP-запросы. Значение этого параметра берется из конфигурации
     *                  приложения с помощью аннотации {@code @Value("${stats-server.url}")}.
     * @param builder   Объект RestTemplateBuilder, который используется для настройки
     *                  RestTemplate.
     */
    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    /**
     * Находит статистику за указанный период времени.
     *
     * @param start  начало периода времени
     * @param end    конец периода времени
     * @param uris   массив URL-адресов, по которым велась статистика
     * @param unique флаг, указывающий, считать ли уникальные хиты
     * @return ответ от сервера статистики
     */
    public ResponseEntity<Object> findStats(LocalDateTime start, LocalDateTime end, String uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                "end", end.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}