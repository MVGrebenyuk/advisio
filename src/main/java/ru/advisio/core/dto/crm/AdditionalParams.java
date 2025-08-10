package ru.advisio.core.dto.crm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Дополнительные параметры отображения в CRM")
public class AdditionalParams {

    @Schema(description = "Уникальный идентификатор")
    private String id;

    @Schema(description = "Техническое наименование")
    private String techName;

    @Schema(description = "Наименование доп.параметра для отображения")
    private String name;

    @Schema(description = "Значение параметра")
    private String value;

    @Schema(description = "Дополнительные параметры")
    private List<AdditionalParams> techParams;

}
