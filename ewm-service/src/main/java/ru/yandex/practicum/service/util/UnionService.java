package ru.yandex.practicum.service.util;

import ru.yandex.practicum.model.*;

import java.time.LocalDateTime;

public interface UnionService {
    /**
     * Получает пользователя по указанному идентификатору, и возвращает его. Если пользователь не найден, генерируется исключение.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект User, представляющий найденного пользователя.
     */
    User getUserOrNotFound(Long userId);

    /**
     * Получает категорию по указанному идентификатору, и возвращает ее. Если категория не найдена, генерируется исключение.
     *
     * @param categoryId Идентификатор категории.
     * @return Объект Category, представляющий найденную категорию.
     */
    Category getCategoryOrNotFound(Long categoryId);

    /**
     * Получает событие по указанному идентификатору, и возвращает его. Если событие не найдено, генерируется исключение.
     *
     * @param eventId Идентификатор события.
     * @return Объект Event, представляющий найденное событие.
     */
    Event getEventOrNotFound(Long eventId);

    /**
     * Получает запрос по указанному идентификатору, и возвращает его. Если запрос не найден, генерируется исключение.
     *
     * @param requestId Идентификатор запроса.
     * @return Объект Request, представляющий найденный запрос.
     */
    Request getRequestOrNotFound(Long requestId);

    /**
     * Получает компиляцию по указанному идентификатору, и возвращает ее. Если компиляция не найдена, генерируется исключение.
     *
     * @param compId Идентификатор компиляции.
     * @return Объект Compilation, представляющий найденную компиляцию.
     */
    Compilation getCompilationOrNotFound(Long compId);

    /**
     * Преобразует строку с датой в объект LocalDateTime. Если строка не является допустимой датой, генерируется исключение.
     *
     * @param date Строка, представляющая дату.
     * @return Объект LocalDateTime, представляющий дату и время.
     */
    LocalDateTime parseDate(String date);
}