package ru.yandex.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import ru.yandex.practicum.service.compilation.CompilationService;
import ru.yandex.practicum.dto.compilation.CompilationDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    /**
     * Получает список компиляций с учетом флага закрепления и пагинацией.
     *
     * @param pinned Флаг, указывающий, нужно ли возвращать только закрепленные компиляции.
     * @param from   Начальная позиция для пагинации.
     * @param size   Количество компиляций на странице.
     * @return Список объектов CompilationDto, представляющих компиляции.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false", name = "pinned") Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all compilations from pinned = {}, and from = {}, size = {}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * Получает информацию о компиляции по ее идентификатору.
     *
     * @param compId Идентификатор компиляции.
     * @return Объект CompilationDto, представляющий информацию о компиляции.
     */
    @GetMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Get Compilation Id {}", compId);
        return compilationService.getCompilationById(compId);
    }
}