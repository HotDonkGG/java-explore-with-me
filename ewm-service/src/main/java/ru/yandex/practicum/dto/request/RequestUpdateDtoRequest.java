package ru.yandex.practicum.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.Status;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateDtoRequest {

    List<Long> requestIds;

    Status status;
}