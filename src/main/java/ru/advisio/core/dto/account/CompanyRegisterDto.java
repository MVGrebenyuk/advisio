package ru.advisio.core.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.advisio.core.enums.CompanyType;

@Data
@Builder
@Schema(description = "ДТО регистрации новой компании")
@ToString
public class CompanyRegisterDto {

    @NotNull
    @Schema(description = "Имя компании")
    private String cname;

    private CompanyType companyType;
}
