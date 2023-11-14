package ru.yandex.practicum.service.user;

import ru.yandex.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);

    /**
     * Возвращает список пользователей в соответствии с предоставленными параметрами.
     *
     * @param ids  Список идентификаторов пользователей, которые необходимо отобразить (опционально).
     * @param from Начальное положение списка при пагинации (неотрицательное число, по умолчанию 0).
     * @param size Количество пользователей для отображения на странице (положительное число, по умолчанию 10).
     * @return Список UserDto, представляющий найденных пользователей.
     */
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}