package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationNewDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.model.Compilation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

    /**
     * Преобразует объект Compilation в объект CompilationDto.
     *
     * @param compilation Объект Compilation, который необходимо преобразовать.
     * @return Объект CompilationDto, представляющий компиляцию с короткой информацией о событиях.
     */
    public static CompilationDto returnCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtoList = EventMapper.returnEventShortDtoList(compilation.getEvents());
        Set<EventShortDto> eventShortDtoSet = new HashSet<>();
        for (EventShortDto shortDto : eventShortDtoList) {
            eventShortDtoSet.add(shortDto);
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventShortDtoSet)
                .build();
    }

    /**
     * Преобразует объект CompilationNewDto в объект Compilation.
     *
     * @param compilationNewDto Объект CompilationNewDto, который необходимо преобразовать.
     * @return Объект Compilation, созданный на основе данных из CompilationNewDto.
     */
    public static Compilation returnCompilation(CompilationNewDto compilationNewDto) {
        return Compilation.builder()
                .title(compilationNewDto.getTitle())
                .pinned(compilationNewDto.getPinned())
                .build();
    }

    /**
     * Преобразует список Compilation в набор CompilationDto.
     *
     * @param compilations Итерируемая коллекция объектов Compilation.
     * @return Набор объектов CompilationDto, представляющих компиляции с короткой информацией о событиях.
     */
    public static Set<CompilationDto> returnCompilationDtoSet(Iterable<Compilation> compilations) {
        Set<CompilationDto> result = new HashSet<>();
        for (Compilation compilation : compilations) {
            result.add(returnCompilationDto(compilation));
        }
        return result;
    }
}