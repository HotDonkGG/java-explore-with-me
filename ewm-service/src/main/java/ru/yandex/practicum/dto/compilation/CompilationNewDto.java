package ru.yandex.practicum.dto.compilation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationNewDto {
    Set<Long> events;
    Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    String title;
}