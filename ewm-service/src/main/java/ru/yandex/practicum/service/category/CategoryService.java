package ru.yandex.practicum.service.category;

import ru.yandex.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    void deleteCategory(Long categoryId);

    /**
     * Получает список категорий с возможностью пагинации.
     *
     * @param from Начальная позиция списка категорий.
     * @param size Количество категорий для возврата.
     * @return Список объектов CategoryDto, представляющих категории.
     */
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);
}