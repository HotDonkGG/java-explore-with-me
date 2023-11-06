package ru.yandex.practicum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static ru.yandex.practicum.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    private long id;
    @NotNull
    @NotBlank
    private String app;
    @NotNull
    @NotBlank
    private String uri;
    @NotNull
    @NotBlank
    private String ip;
    @NotNull
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    LocalDateTime timestamp;
}