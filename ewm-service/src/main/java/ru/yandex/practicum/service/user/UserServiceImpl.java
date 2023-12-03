package ru.yandex.practicum.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mapper.UserMapper;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.dto.user.UserDto;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.util.UnionService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UnionService unionService;

    /**
     * Создает нового пользователя на основе предоставленных данных.
     *
     * @param userDto Объект UserDto, представляющий нового пользователя.
     * @return Объект UserDto, представляющий созданного пользователя.
     */
    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.returnUser(userDto);
        userRepository.save(user);
        return UserMapper.returnUserDto(user);
    }

    /**
     * Возвращает список пользователей в соответствии с предоставленными параметрами.
     *
     * @param ids  Список идентификаторов пользователей, которые необходимо отобразить (опционально).
     * @param from Начальное положение списка при пагинации (неотрицательное число, по умолчанию 0).
     * @param size Количество пользователей для отображения на странице (положительное число, по умолчанию 10).
     * @return Список UserDto, представляющий найденных пользователей.
     */
    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (ids == null) {
            return UserMapper.returnUserDtoList(userRepository.findAll(pageRequest));
        } else {
            return UserMapper.returnUserDtoList(userRepository.findByIdInOrderByIdAsc(ids, pageRequest));
        }
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        unionService.getUserOrNotFound(userId);
        userRepository.deleteById(userId);
    }
}