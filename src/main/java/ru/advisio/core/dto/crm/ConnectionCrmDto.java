package ru.advisio.core.dto.crm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.enums.CrmType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionCrmDto {

    @NotBlank
    private String connectionName;

    @NotNull
    private CrmType crmType;

    @NotNull
    private ConnectionType connectionType;

    @NotBlank
    private String connectionUrl;

    private String login;

    private String password;

    private String token;

}
