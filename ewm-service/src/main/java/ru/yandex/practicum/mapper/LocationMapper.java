package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dto.location.LocationDto;
import ru.yandex.practicum.model.Location;

@UtilityClass
public class LocationMapper {

    /**
     * Преобразует объект Location в объект LocationDto.
     *
     * @param location Объект Location, который необходимо преобразовать.
     * @return Объект LocationDto, представляющий преобразованные данные из Location.
     */
    public LocationDto returnLocationDto(Location location) {
        LocationDto locationDto = LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
        return locationDto;
    }

    /**
     * Преобразует объект LocationDto в объект Location.
     *
     * @param locationDto Объект LocationDto, который необходимо преобразовать.
     * @return Объект Location, представляющий преобразованные данные из LocationDto.
     */
    public Location returnLocation(LocationDto locationDto) {
        Location location = Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
        return location;
    }
}