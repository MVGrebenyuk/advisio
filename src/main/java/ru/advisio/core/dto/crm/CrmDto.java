package ru.advisio.core.dto.crm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.enums.CrmType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrmDto {

    private UUID id;

    private String name;

    private CrmType crmType;

    private String description;

    @Schema(description = "Не заполняется, если crmType == local")
    private ConnectionCrmDto connectionParams;

}
