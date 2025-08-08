package ru.advisio.core.dto.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDto {

    private String id;

    @Schema(description = "Ссылка на шаблон на s3 хранилище")
    private String url;

    @Schema(description = "Наименование шаблона")
    private String name;

    @Schema(description = "Дата первой загрузки")
    private LocalDateTime dateOfLoad;

}
