package ru.advisio.core.dto.salepoints;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.entity.Image;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Торговая точка")
public class SalePointDto {

    private String id;

    @Schema(description = "Наименование торговой точки")
    private String name;

    @Schema(description = "Адрес торговой точки")
    private String address;

    @Schema(description = "Дополнительная информация о торговой точке")
    private String description;

    @Schema(description = "Изображения, привязанные к ТТ")
    private List<Image> images;

}
