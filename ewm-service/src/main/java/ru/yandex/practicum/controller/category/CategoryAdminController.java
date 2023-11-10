package ru.yandex.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.category.CategoryService;
import ru.yandex.practicum.dto.category.CategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    /**
     * Добавляет новую категорию.
     *
     * @param categoryDto Объект, представляющий новую категорию.
     * @return Объект CategoryDto, представляющий информацию о созданной категории.
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Add Category {} ", categoryDto.getName());
        return categoryService.addCategory(categoryDto);
    }

    /**
     * Обновляет информацию о категории по ее идентификатору.
     *
     * @param categoryDto Объект, содержащий данные для обновления категории.
     * @param categoryId  Идентификатор категории, которую необходимо обновить.
     * @return Объект CategoryDto, представляющий обновленную информацию о категории.
     */
    @PatchMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @PathVariable("catId") Long categoryId) {
        log.info("Update Category {} ", categoryDto.getName());
        return categoryService.updateCategory(categoryDto, categoryId);
    }

    /**
     * Удаляет категорию по ее идентификатору.
     *
     * @param categoryId Идентификатор категории, которую необходимо удалить.
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long categoryId) {
        log.info("Delete Category {} ", categoryId);
        categoryService.deleteCategory(categoryId);
    }
}