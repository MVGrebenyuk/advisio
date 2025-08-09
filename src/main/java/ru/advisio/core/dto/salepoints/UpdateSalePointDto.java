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
@Schema(description = "ДТО обновления торговой точки")
public class UpdateSalePointDto {

    @Schema(description = "Обновленное имя торговой точки")
    private String updatedName;

    @Schema(description = "Список привязанных картинок к ТТ ( can be nullable)")
    private List<Image> imageList;

}
