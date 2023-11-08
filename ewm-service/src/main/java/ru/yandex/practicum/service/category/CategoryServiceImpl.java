package ru.yandex.practicum.service.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.dto.category.CategoryDto;
import ru.yandex.practicum.mapper.CategoryMapper;
import ru.yandex.practicum.model.Category;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.service.util.UnionService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;
    private UnionService unionService;

    /**
     * Добавляет новую категорию.
     *
     * @param categoryDto Данные категории для добавления.
     * @return Объект CategoryDto, представляющий добавленную категорию.
     */
    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.returnCategory(categoryDto);
        categoryRepository.save(category);
        return CategoryMapper.returnCategoryDto(category);
    }

    /**
     * Обновляет существующую категорию по её идентификатору.
     *
     * @param categoryDto Данные категории для обновления.
     * @param categoryId  Идентификатор существующей категории, которую необходимо обновить.
     * @return Объект CategoryDto, представляющий обновленную категорию.
     */
    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = unionService.getCategoryOrNotFound(categoryId);
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return CategoryMapper.returnCategoryDto(category);
    }

    /**
     * Удаляет категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории, которую необходимо удалить.
     */
    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        unionService.getCategoryOrNotFound(categoryId);
        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new ConflictException(String.format("This category id %s is used and cannot be deleted", categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }

    /**
     * Получает список категорий с возможностью пагинации.
     *
     * @param from Начальная позиция списка категорий.
     * @param size Количество категорий для возврата.
     * @return Список объектов CategoryDto, представляющих категории.
     */
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return CategoryMapper.returnCategoryDtoList(categoryRepository.findAll(pageRequest));
    }

    /**
     * Получает категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории, которую необходимо получить.
     * @return Объект CategoryDto, представляющий запрошенную категорию.
     */
    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.returnCategoryDto(unionService.getCategoryOrNotFound(categoryId));
    }
}