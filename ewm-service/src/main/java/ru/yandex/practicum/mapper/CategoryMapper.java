package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.category.CategoryDto;
import ru.yandex.practicum.model.Category;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {
    /**
     * Преобразует объект типа Category в объект типа CategoryDto.
     *
     * @param category Объект типа Category, который необходимо преобразовать.
     * @return Объект типа CategoryDto, представляющий информацию о категории.
     */
    public static CategoryDto returnCategoryDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryDto;
    }

    /**
     * Преобразует объект типа CategoryDto в объект типа Category.
     *
     * @param categoryDto Объект типа CategoryDto, который необходимо преобразовать.
     * @return Объект типа Category, представляющий информацию о категории.
     */
    public static Category returnCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
        return category;
    }

    /**
     * Преобразует список объектов типа Category в список объектов типа CategoryDto.
     *
     * @param categories Список объектов типа Category, который необходимо преобразовать.
     * @return Список объектов типа CategoryDto, представляющих информацию о категориях.
     */
    public static List<CategoryDto> returnCategoryDtoList(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(returnCategoryDto(category));
        }
        return result;
    }
}