package ru.yandex.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import ru.yandex.practicum.dto.user.UserDto;
import ru.yandex.practicum.service.user.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    /**
     * Создает нового пользователя на основе предоставленных данных.
     *
     * @param userDto Объект UserDto, представляющий нового пользователя.
     * @return Объект UserDto, представляющий созданного пользователя.
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Add User {} ", userDto.getName());
        return userService.addUser(userDto);
    }

    /**
     * Возвращает список пользователей в соответствии с предоставленными параметрами.
     *
     * @param ids  Список идентификаторов пользователей, которые необходимо отобразить (опционально).
     * @param from Начальное положение списка при пагинации (неотрицательное число, по умолчанию 0).
     * @param size Количество пользователей для отображения на странице (положительное число, по умолчанию 10).
     * @return Список UserDto, представляющий найденных пользователей.
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("List Users, where ids: {}, from = {}, size = {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    /**
     * Удаляет пользователя с указанным идентификатором.
     *
     * @param userId Идентификатор пользователя, который будет удален.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable("userId") Long userId) {
        log.info("User {} deleted ", userId);
        userService.deleteUser(userId);
    }
}