package ru.yandex.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationNewDto;
import ru.yandex.practicum.service.compilation.CompilationService;
import ru.yandex.practicum.dto.compilation.CompilationUpdateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    /**
     * Добавляет новую компиляцию.
     *
     * @param compilationNewDto Объект, представляющий новую компиляцию.
     * @return Объект CompilationDto, представляющий информацию о созданной компиляции.
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("Add Compilation {} ", compilationNewDto.getTitle());
        return compilationService.addCompilation(compilationNewDto);
    }

    /**
     * Удаляет компиляцию по ее идентификатору.
     *
     * @param compId Идентификатор компиляции, которую необходимо удалить.
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("Delete Compilation {} ", compId);
        compilationService.deleteCompilation(compId);
    }

    /**
     * Обновляет информацию о компиляции по ее идентификатору.
     *
     * @param compilationUpdateDto Объект, содержащий данные для обновления компиляции.
     * @param compId               Идентификатор компиляции, которую необходимо обновить.
     * @return Объект CompilationDto, представляющий обновленную информацию о компиляции.
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto updateCompilation(@Valid @RequestBody CompilationUpdateDto compilationUpdateDto,
                                            @PathVariable Long compId) {
        log.info("Update Compilation {} ", compId);
        return compilationService.updateCompilation(compId, compilationUpdateDto);
    }
}