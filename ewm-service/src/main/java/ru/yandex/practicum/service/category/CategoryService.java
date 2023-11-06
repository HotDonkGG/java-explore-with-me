package ru.yandex.practicum.service.category;

import ru.yandex.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * Добавляет новую категорию.
     *
     * @param categoryDto Данные категории для добавления.
     * @return Объект CategoryDto, представляющий добавленную категорию.
     */
    CategoryDto addCategory(CategoryDto categoryDto);

    /**
     * Обновляет существующую категорию по её идентификатору.
     *
     * @param categoryDto Данные категории для обновления.
     * @param categoryId  Идентификатор существующей категории, которую необходимо обновить.
     * @return Объект CategoryDto, представляющий обновленную категорию.
     */
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    /**
     * Удаляет категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории, которую необходимо удалить.
     */
    void deleteCategory(Long categoryId);

    /**
     * Получает список категорий с возможностью пагинации.
     *
     * @param from Начальная позиция списка категорий.
     * @param size Количество категорий для возврата.
     * @return Список объектов CategoryDto, представляющих категории.
     */
    List<CategoryDto> getCategories(Integer from, Integer size);

    /**
     * Получает категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории, которую необходимо получить.
     * @return Объект CategoryDto, представляющий запрошенную категорию.
     */
    CategoryDto getCategoryById(Long categoryId);
}