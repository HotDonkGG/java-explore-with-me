package ru.yandex.practicum.service.user;

import ru.yandex.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}