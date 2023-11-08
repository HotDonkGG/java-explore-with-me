package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;

import ru.yandex.practicum.dto.user.UserDto;
import ru.yandex.practicum.dto.user.UserShortDto;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {
    /**
     * Преобразует объект типа User в объект типа UserDto.
     *
     * @param user Объект типа User, который необходимо преобразовать.
     * @return Объект типа UserDto, представляющий информацию о пользователе.
     */
    public static UserDto returnUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return userDto;
    }

    /**
     * Преобразует объект типа User в объект типа UserShortDto.
     *
     * @param user Объект типа User, который необходимо преобразовать.
     * @return Объект типа UserShortDto, представляющий краткую информацию о пользователе.
     */
    public static UserShortDto returnUserShortDto(User user) {
        UserShortDto userShortDto = UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
        return userShortDto;
    }

    /**
     * Преобразует объект типа UserDto в объект типа User.
     *
     * @param userDto Объект типа UserDto, который необходимо преобразовать.
     * @return Объект типа User, представляющий информацию о пользователе.
     */
    public static User returnUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
        return user;
    }

    /**
     * Преобразует список объектов типа User в список объектов типа UserDto.
     *
     * @param users Список объектов типа User, которые необходимо преобразовать.
     * @return Список объектов типа UserDto, представляющих информацию о пользователях.
     */
    public static List<UserDto> returnUserDtoList(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(returnUserDto(user));
        }
        return result;
    }
}