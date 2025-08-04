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
@Schema(description = "ДТО регистрации нового аккаунта")
@ToString
public class AccountRegisterDto {

    @Email
    @Schema(description = "Email аккаунта, на который будет происходить регистрация")
    private String email;

    @NotNull
    @Schema(description = "Телефон аккаунта, на который будет происходить регистрация")
    private String phone;

    private CompanyType companyType;
}
