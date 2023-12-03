package ru.yandex.practicum.service.compilation;

import ru.yandex.practicum.dto.compilation.CompilationUpdateDto;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationNewDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(CompilationNewDto compilationNewDto);

    void deleteCompilation(Long compId);

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

    CompilationDto getCompilationById(Long compId);
}