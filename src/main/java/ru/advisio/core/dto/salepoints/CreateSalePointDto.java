package ru.advisio.core.dto.salepoints;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "ДТО для создания компании")
public class CreateSalePointDto {

    @Schema(description = "Описание компании")
    private String name;

}
