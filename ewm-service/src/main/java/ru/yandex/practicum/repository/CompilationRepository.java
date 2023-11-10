package ru.yandex.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Ищет компиляции с учетом флага "pinned" и предоставляет их с пагинацией.
     *
     * @param pinned      Флаг, указывающий, является ли компиляция закрепленной.
     * @param pageRequest Запрос страницы, определяющий начальную позицию и размер страницы.
     * @return Список компиляций, удовлетворяющих заданным критериям, с примененной пагинацией.
     */
    List<Compilation> findByPinned(Boolean pinned, PageRequest pageRequest);
}