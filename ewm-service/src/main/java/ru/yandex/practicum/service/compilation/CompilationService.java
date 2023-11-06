package ru.yandex.practicum.service.compilation;

import ru.yandex.practicum.dto.compilation.CompilationUpdateDto;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationNewDto;

import java.util.List;

public interface CompilationService {

    /**
     * Добавляет новую компиляцию событий.
     *
     * @param compilationNewDto Данные для создания новой компиляции.
     * @return Объект CompilationDto, представляющий добавленную компиляцию.
     */
    CompilationDto addCompilation(CompilationNewDto compilationNewDto);

    /**
     * Удаляет компиляцию по её идентификатору.
     *
     * @param compId Идентификатор компиляции, которую необходимо удалить.
     */
    void deleteCompilation(Long compId);

    /**
     * Обновляет существующую компиляцию по её идентификатору.
     *
     * @param compId               Идентификатор существующей компиляции, которую необходимо обновить.
     * @param compilationUpdateDto Данные для обновления компиляции.
     * @return Объект CompilationDto, представляющий обновленную компиляцию.
     */
    CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto);

    /**
     * Получает список компиляций событий с возможностью фильтрации по закрепленным и с пагинацией.
     *
     * @param pinned Флаг, указывающий на закрепленные компиляции.
     * @param from   Начальная позиция списка компиляций.
     * @param size   Количество компиляций для возврата.
     * @return Список объектов CompilationDto, представляющих компиляции событий.
     */
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Получает компиляцию по её идентификатору.
     *
     * @param compId Идентификатор компиляции, которую необходимо получить.
     * @return Объект CompilationDto, представляющий запрошенную компиляцию.
     */
    CompilationDto getCompilationById(Long compId);
}