package ru.advisio.core.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.dto.device.enums.Source;
import ru.advisio.core.entity.Tag;
import ru.advisio.core.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Device dto")
public class DeviceDto {

    @Schema(description = "id устройства")
    private String id;

    @Schema(description = "Серийный номер устройства")
    private String serial;

    @Schema(description = "Источник изображения")
    private Source source;

    @Schema(description = "Статус")
    private Status status;

    @Schema(description = "Торговая точка девайса")
    private SalePointDto salePoint;

    @Schema(description = "Статус активности устройства")
    private boolean isActive;

    @Schema(description = "Тэг устройства")
    private Tag tag;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TagDto {
        private String id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SalePointDto {
        private String id;
        private String name;
    }

}
