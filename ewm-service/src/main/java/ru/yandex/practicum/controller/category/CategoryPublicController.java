package ru.yandex.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import ru.yandex.practicum.dto.category.CategoryDto;
import ru.yandex.practicum.service.category.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    /**
     * Возвращает список категорий с публичным доступом.
     *
     * @param from Начальная позиция списка категорий (по умолчанию 0).
     * @param size Количество категорий, возвращаемых в результате (по умолчанию 10).
     * @return Список объектов CategoryDto, представляющих информацию о категориях.
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("List Categories, where: from = {}, size = {}", from, size);
        return categoryService.getCategories(from, size);
    }

    /**
     * Возвращает информацию о категории по ее идентификатору.
     *
     * @param categoryId Идентификатор категории, информацию о которой необходимо получить.
     * @return Объект CategoryDto, представляющий информацию о категории.
     */
    @GetMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable("catId") Long categoryId) {
        log.info("Get Category id {}", categoryId);
        return categoryService.getCategoryById(categoryId);
    }
}