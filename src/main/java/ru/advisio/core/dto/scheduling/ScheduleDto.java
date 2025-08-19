package ru.advisio.core.dto.scheduling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {

    List<Short> days;

    Long startTime;

    Long endTime;

}
