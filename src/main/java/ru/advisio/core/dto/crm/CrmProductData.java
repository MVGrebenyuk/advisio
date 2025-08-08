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
@Schema(name = "Модель данных для вывода данных из CRM")
public class CrmProductData {

    @Schema(description = "Уникальный идентификатор продукта")
    private String id;

    @Schema(description = "Наименование продукта в crm")
    private String techName;

    @Schema(description = "Наименование для отображения")
    private String saleName;

    @Schema(description = "Стоимость продукта")
    private String value;

    @Schema(description = "Дополнительные параметры")
    List<AdditionalParams> additional;

}
