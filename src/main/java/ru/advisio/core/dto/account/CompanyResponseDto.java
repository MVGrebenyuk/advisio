package ru.advisio.core.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.enums.CompanyType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание компании")
public class CompanyResponseDto {

    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Наименование компании")
    private String cname;

    private CompanyType companyType;
}
