package ru.advisio.core.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.dto.scheduling.ScheduleDto;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseDto {

    private UUID id;

    private String image;

    @Schema(description = "Расписание показа изображения в рамках дней недели\\часов\\минут")
    private ScheduleDto schedule;
}
