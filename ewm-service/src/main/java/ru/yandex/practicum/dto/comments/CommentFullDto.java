package ru.yandex.practicum.dto.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.user.UserDto;

import java.time.LocalDateTime;

import static ru.yandex.practicum.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto {

    Long id;

    UserDto user;

    EventFullDto event;

    String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    LocalDateTime created;
}