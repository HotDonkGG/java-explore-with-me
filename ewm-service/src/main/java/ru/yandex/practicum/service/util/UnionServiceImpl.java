package ru.yandex.practicum.service.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.yandex.practicum.Util.FORMATTER;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UnionServiceImpl implements UnionService {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;
    private RequestRepository requestRepository;
    private CompilationRepository compilationRepository;

    /**
     * Получает пользователя по указанному идентификатору и возвращает его. Если пользователь не найден, генерируется исключение.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект User, представляющий найденного пользователя.
     * @throws NotFoundException если пользователь с указанным идентификатором не найден.
     */
    @Override
    public User getUserOrNotFound(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        } else {
            return user.get();
        }
    }

    /**
     * Получает категорию по указанному идентификатору и возвращает ее. Если категория не найдена, генерируется исключение.
     *
     * @param categoryId Идентификатор категории.
     * @return Объект Category, представляющий найденную категорию.
     * @throws NotFoundException если категория с указанным идентификатором не найдена.
     */
    @Override
    public Category getCategoryOrNotFound(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException(Category.class, "Category id " + categoryId + " not found.");
        } else {
            return category.get();
        }
    }

    /**
     * Получает событие по указанному идентификатору и возвращает его. Если событие не найдено, генерируется исключение.
     *
     * @param eventId Идентификатор события.
     * @return Объект Event, представляющий найденное событие.
     * @throws NotFoundException если событие с указанным идентификатором не найдено.
     */
    @Override
    public Event getEventOrNotFound(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException(Event.class, "Event id " + eventId + " not found.");
        } else {
            return event.get();
        }
    }

    /**
     * Получает запрос по указанному идентификатору и возвращает его. Если запрос не найден, генерируется исключение.
     *
     * @param requestId Идентификатор запроса.
     * @return Объект Request, представляющий найденный запрос.
     * @throws NotFoundException если запрос с указанным идентификатором не найден.
     */
    @Override
    public Request getRequestOrNotFound(Long requestId) {

        Optional<Request> request = requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new NotFoundException(Request.class, "Request id " + requestId + " not found.");
        } else {
            return request.get();
        }
    }

    /**
     * Получает компиляцию по указанному идентификатору и возвращает ее. Если компиляция не найдена, генерируется исключение.
     *
     * @param compId Идентификатор компиляции.
     * @return Объект Compilation, представляющий найденную компиляцию.
     * @throws NotFoundException если компиляция с указанным идентификатором не найдена.
     */
    @Override
    public Compilation getCompilationOrNotFound(Long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        if (compilation.isEmpty()) {
            throw new NotFoundException(Compilation.class, "Compilation id " + compId + " not found.");
        } else {
            return compilation.get();
        }
    }

    /**
     * Преобразует строку с датой в объект LocalDateTime. Если строка не является допустимой датой, генерируется исключение.
     *
     * @param date Строка, представляющая дату.
     * @return Объект LocalDateTime, представляющий дату и время.
     * @throws NotFoundException если строка не может быть преобразована в дату и время.
     */
    @Override
    public LocalDateTime parseDate(String date) {
        if (date != null) {
            return LocalDateTime.parse(date, FORMATTER);
        } else {
            return null;
        }
    }
}