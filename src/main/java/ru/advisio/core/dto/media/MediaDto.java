package ru.advisio.core.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.dto.group.GroupDto;
import ru.advisio.core.dto.image.ImageDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDto {

    @Schema(description = "1k1 c imageId")
    private String id;

    private String name;

    private String url;

    @Schema(description = "Использованные изображения и их настройки")
    List<ImageDto> images;

    @Schema(description = "Шаблон, на основе которого создано медия")
    private ShortTemplateDto template;

    @Schema(description = "Активные девайсы с текущим изображением")
    private ImageActiveDevices activeDevices;

    @Schema(description = "Используется ли где-то это медиа")
    private Boolean isActive;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShortTemplateDto {
        private String uuid;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageActiveDevices {
        @Schema(description = "Связка SalePoint : девайсы")
        Map<String, Set<ShortDeviceDto>> map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShortDeviceDto {
        private String id;
        private boolean isActive;
        private String name;
    }

}
