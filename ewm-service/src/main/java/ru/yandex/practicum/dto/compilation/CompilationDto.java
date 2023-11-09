package ru.yandex.practicum.dto.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.dto.event.EventShortDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Long id;

    Boolean pinned;

    String title;

    Set<EventShortDto> events;
}