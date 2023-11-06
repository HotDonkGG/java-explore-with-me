package ru.yandex.practicum.service.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.compilation.CompilationUpdateDto;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationNewDto;
import ru.yandex.practicum.mapper.CompilationMapper;
import ru.yandex.practicum.model.Compilation;
import ru.yandex.practicum.repository.CompilationRepository;
import ru.yandex.practicum.service.util.UnionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;
    private UnionService unionService;

    /**
     * Добавляет новую компиляцию событий.
     *
     * @param compilationNewDto Данные для создания новой компиляции.
     * @return Объект CompilationDto, представляющий добавленную компиляцию.
     */
    @Override
    @Transactional
    public CompilationDto addCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = CompilationMapper.returnCompilation(compilationNewDto);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (compilationNewDto.getEvents() == null || compilationNewDto.getEvents().isEmpty()) {
            compilation.setEvents(Collections.emptySet());
        } else {
            compilation.setEvents(eventRepository.findByIdIn(compilationNewDto.getEvents()));
        }
        compilation = compilationRepository.save(compilation);
        return CompilationMapper.returnCompilationDto(compilation);
    }

    /**
     * Удаляет компиляцию по её идентификатору.
     *
     * @param compId Идентификатор компиляции, которую необходимо удалить.
     */
    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        unionService.getCompilationOrNotFound(compId);
        compilationRepository.deleteById(compId);
    }

    /**
     * Обновляет существующую компиляцию по её идентификатору.
     *
     * @param compId               Идентификатор существующей компиляции, которую необходимо обновить.
     * @param compilationUpdateDto Данные для обновления компиляции.
     * @return Объект CompilationDto, представляющий обновленную компиляцию.
     */
    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = unionService.getCompilationOrNotFound(compId);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (compilationUpdateDto.getEvents() == null || compilationUpdateDto.getEvents().isEmpty()) {
            compilation.setEvents(Collections.emptySet());
        } else {
            compilation.setEvents(eventRepository.findByIdIn(compilationUpdateDto.getEvents()));
        }
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        compilation = compilationRepository.save(compilation);
        return CompilationMapper.returnCompilationDto(compilation);
    }

    /**
     * Получает список компиляций событий с возможностью фильтрации по закрепленным и с пагинацией.
     *
     * @param pinned Флаг, указывающий на закрепленные компиляции.
     * @param from   Начальная позиция списка компиляций.
     * @param size   Количество компиляций для возврата.
     * @return Список объектов CompilationDto, представляющих компиляции событий.
     */
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        compilations = compilationRepository.findByPinned(pinned, pageRequest);
        return new ArrayList<>(CompilationMapper.returnCompilationDtoSet(compilations));
    }

    /**
     * Получает компиляцию по её идентификатору.
     *
     * @param compId Идентификатор компиляции, которую необходимо получить.
     * @return Объект CompilationDto, представляющий запрошенную компиляцию.
     */
    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = unionService.getCompilationOrNotFound(compId);
        return CompilationMapper.returnCompilationDto(compilation);
    }
}